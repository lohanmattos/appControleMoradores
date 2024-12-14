package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.Receita;
import dev.amendola.appControleMoradores.Model.TipoCategoria;
import dev.amendola.appControleMoradores.Service.CategoriaContasService;
import dev.amendola.appControleMoradores.Service.ConfigCondominioService;
import dev.amendola.appControleMoradores.Service.ReceitaService;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import dev.amendola.appControleMoradores.Model.CategoriaContas;
import dev.amendola.appControleMoradores.Model.ConfigCondominio;

@Controller
@RequestMapping("/receitas")
public class ReceitaController {

    private final ReceitaService receitaService;
    
    @Autowired
    private CategoriaContasService categoriaContasService;
    
    @Autowired
    private ConfigCondominioService configCondominioService;

    public ReceitaController(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }
   

    // Exibe a lista de receitas e inicializa o formulário no modal
    @GetMapping
    public String listarReceitas(Model model) {
        model.addAttribute("receitas", receitaService.listarTodas());
        model.addAttribute("receita", new Receita()); // Inicializa o objeto para o modal

        List<CategoriaContas> categoriasReceitas = categoriaContasService.listarPorTipo(TipoCategoria.RECEITA);

        model.addAttribute("categoriasReceitas", categoriasReceitas);
        
        return "financeiro/receitas/receitas"; // Redireciona para o template receitas/lista.html
    }

    
    @PostMapping
    public String cadastrarReceita(@ModelAttribute("receita") Receita receita, RedirectAttributes redirectAttributes) {
        try {
            receitaService.salvar(receita);
            redirectAttributes.addAttribute("success", "Receita cadastrada com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Erro ao cadastrar a receita.");
        }
        return "redirect:/receitas";
    }
    
   
    
    @GetMapping("/excluir/{id}")
    public String excluirDespesa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            receitaService.deletar(id);
            redirectAttributes.addAttribute("success", "Receita excluída com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Erro ao excluir a receita.");
        }
        return "redirect:/receitas";
    }
    
    @GetMapping("/filtro")
    public String listarReceitas(@RequestParam(required = false) LocalDate dataInicio,
                                 @RequestParam(required = false) LocalDate dataFim,
                                 @RequestParam(required = false) Long categoria,
                                 @RequestParam(required = false) Boolean pago,
                                 Model model) {

        // Simulação de lista de receitas
        List<Receita> todasReceitas = receitaService.listarTodas(); // Adicione seu método de serviço

        // Aplicar filtros
        List<Receita> receitasFiltradas = todasReceitas.stream()
                .filter(receita -> (dataInicio == null || !receita.getData().isBefore(dataInicio)))
                .filter(receita -> (dataFim == null || !receita.getData().isAfter(dataFim)))
                .filter(receita -> (categoria == null || (receita.getCategoria() != null && receita.getCategoria().getId().equals(categoria))))
                .filter(receita -> (pago == null || receita.isPago() == pago))
                .collect(Collectors.toList());

        // Passar dados para o modelo
        model.addAttribute("receitas", receitasFiltradas);
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);
        model.addAttribute("categoria", categoria);
        model.addAttribute("pago", pago);
        model.addAttribute("categoriasReceitas", categoriaContasService.listarTodos()); // Lista de categorias

        // Adiciona um novo objeto Receita ao modelo para o formulário
        model.addAttribute("receita", new Receita());
        
        return "financeiro/receitas/receitas"; // Nome do arquivo HTML
    }
    
    @GetMapping("/pdf")
    public void gerarRelatorioReceitas(HttpServletResponse response) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            // Configurar resposta HTTP
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=relatorio_receitas.pdf");

            // Criar documento PDF
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Configurar fontes
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fontTexto = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

            // Cabeçalho do relatório
            ConfigCondominio config = configCondominioService.buscarConfiguracoes();
            if (config != null) {
                document.add(new Paragraph(config.getNomeCondominio(), fontTitulo));
                document.add(new Paragraph(config.getEndereco() + ", " + config.getCidade() + " - " + config.getEstado(), fontTexto));
            }

            document.add(new Paragraph("\n"));
            document.add(new LineSeparator());
            document.add(new Paragraph("\n"));

            // Título do Relatório
            Paragraph titulo = new Paragraph("Relatório de Receitas", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph("\n"));

            // Tabela de Receitas
            PdfPTable tabelaReceitas = new PdfPTable(4); // Tabela com 4 colunas
            tabelaReceitas.setWidthPercentage(100);
            tabelaReceitas.setSpacingBefore(10f);
            tabelaReceitas.addCell(criarCelula("Data", fontSubtitulo));
            tabelaReceitas.addCell(criarCelula("Descrição", fontSubtitulo));
            tabelaReceitas.addCell(criarCelula("Categoria", fontSubtitulo));
            tabelaReceitas.addCell(criarCelula("Valor", fontSubtitulo));

            List<Receita> receitas = receitaService.listarTodas(); // Buscar receitas do banco
            BigDecimal totalReceitas = BigDecimal.ZERO;
            for (Receita receita : receitas) {
                tabelaReceitas.addCell(receita.getData().format(formatter));
                tabelaReceitas.addCell(receita.getDescricao());
                tabelaReceitas.addCell(receita.getCategoria() != null ? receita.getCategoria().getNome() : "N/A");
                tabelaReceitas.addCell("R$ " + receita.getValor());
                totalReceitas = totalReceitas.add(receita.getValor());
            }
            document.add(tabelaReceitas);

            // Total de Receitas
            Paragraph totalReceitasP = new Paragraph(String.format("Total de Receitas: R$ %.2f", totalReceitas), fontTexto);
            totalReceitasP.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalReceitasP);

            document.add(new Paragraph("\n"));

            // Assinatura com Local e Data
            LocalDate dataAtual = LocalDate.now();
            
            String localEData = config.getCidade() + " - " + config.getEstado() + ", " + dataAtual.format(formatter);

            Paragraph assinatura = new Paragraph(
                    "___________________________________________\n" +
                            (config != null && config.getSindico() != null ? config.getSindico().getNome() : "Síndico") +
                            "\n" + localEData,
                    fontTexto
            );
            assinatura.setAlignment(Element.ALIGN_LEFT);
            document.add(assinatura);

            document.add(new Paragraph("\n"));
            document.add(new LineSeparator());
            Paragraph rodape = new Paragraph("CorujaCondo - Sistema de Gestão de Condomínios.", fontTexto);
            rodape.setAlignment(Element.ALIGN_CENTER);
            document.add(rodape);
            Paragraph rodapeDev = new Paragraph("Desenvolvido: Lohan Amendola", fontTexto);
            rodapeDev.setAlignment(Element.ALIGN_CENTER);
            document.add(rodapeDev);

            document.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private PdfPCell criarCelula(String conteudo, Font fonte) {
        PdfPCell celula = new PdfPCell(new Phrase(conteudo, fonte));
        celula.setHorizontalAlignment(Element.ALIGN_LEFT);
        celula.setPadding(2);
        celula.setBackgroundColor(BaseColor.GRAY); // Fundo cinza
        celula.setPhrase(new Phrase(conteudo, new Font(fonte.getFamily(), fonte.getSize(), Font.BOLD, BaseColor.WHITE))); // Texto branco
        return celula;
    }

}
