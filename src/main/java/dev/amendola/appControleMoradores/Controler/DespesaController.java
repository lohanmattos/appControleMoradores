package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.CategoriaContas;
import dev.amendola.appControleMoradores.Model.ConfigCondominio;
import dev.amendola.appControleMoradores.Model.Despesa;
import dev.amendola.appControleMoradores.Model.Receita;
import dev.amendola.appControleMoradores.Model.TipoCategoria;
import dev.amendola.appControleMoradores.Service.CategoriaContasService;
import dev.amendola.appControleMoradores.Service.ConfigCondominioService;
import dev.amendola.appControleMoradores.Service.DespesaService;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/despesas")
public class DespesaController {

    private final DespesaService despesaService;
    
    @Autowired
    private CategoriaContasService categoriaContasService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }
    
    @Autowired
    private ConfigCondominioService configCondominioService;

    @GetMapping
    public String listarDespesas(Model model) {
        model.addAttribute("despesas", despesaService.listarTodas());
        model.addAttribute("despesa", new Despesa()); // Para o modal
        
        List<CategoriaContas> categoriasDespesas = categoriaContasService.listarPorTipo(TipoCategoria.DESPESA);

        model.addAttribute("categoriasDespesas", categoriasDespesas);
        return "financeiro/despesas/despesas";
    }

 // Exemplo de controlador em Spring
    @PostMapping
    public String cadastrarDespesa(@ModelAttribute("despesa") Despesa despesa, RedirectAttributes redirectAttributes) {
        try {
            despesaService.salvar(despesa);
            redirectAttributes.addAttribute("success", "Despesa cadastrada com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Erro ao cadastrar a despesa.");
        }
        return "redirect:/despesas";
    }

    @GetMapping("/excluir/{id}")
    public String excluirDespesa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            despesaService.deletar(id);
            redirectAttributes.addAttribute("success", "Despesa excluída com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Erro ao excluir a despesa.");
        }
        return "redirect:/despesas";
    }
    
    @GetMapping("/filtro")
    public String listarDespesas(@RequestParam(required = false) LocalDate dataInicio,
                                 @RequestParam(required = false) LocalDate dataFim,
                                 @RequestParam(required = false) Long categoria,
                                 @RequestParam(required = false) Boolean pago,
                                 Model model) {

        // Obter todas as despesas
        List<Despesa> todasDespesas = despesaService.listarTodas();

        // Aplicar filtros apenas se os parâmetros forem fornecidos
        List<Despesa> despesasFiltradas = todasDespesas.stream()
                .filter(despesa -> (dataInicio == null || !despesa.getData().isBefore(dataInicio)))
                .filter(despesa -> (dataFim == null || !despesa.getData().isAfter(dataFim)))
                .filter(despesa -> (categoria == null || (despesa.getCategoria() != null && despesa.getCategoria().getId().equals(categoria))))
                .filter(despesa -> (pago == null || despesa.isPago() == pago))
                .collect(Collectors.toList());

        // Passar dados para o modelo
        model.addAttribute("despesas", despesasFiltradas.isEmpty() ? todasDespesas : despesasFiltradas);
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);
        model.addAttribute("categoria", categoria);
        model.addAttribute("pago", pago);
        model.addAttribute("categoriasDespesas", categoriaContasService.listarTodos());
        
        // Adiciona um novo objeto Receita ao modelo para o formulário
        model.addAttribute("despesa", new Despesa());

        return "financeiro/despesas/despesas"; // Nome do arquivo HTML
    }
    
    @GetMapping("/pdf")
    public void gerarRelatorioDespesas(HttpServletResponse response) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            // Configurar resposta HTTP
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=relatorio_despesas.pdf");

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
            Paragraph titulo = new Paragraph("Relatório de Despesas", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph("\n"));

            // Tabela de Despesas
            PdfPTable tabelaDespesas = new PdfPTable(4); // Tabela com 4 colunas
            tabelaDespesas.setWidthPercentage(100);
            tabelaDespesas.setSpacingBefore(10f);
            tabelaDespesas.addCell(criarCelula("Data", fontSubtitulo));
            tabelaDespesas.addCell(criarCelula("Descrição", fontSubtitulo));
            tabelaDespesas.addCell(criarCelula("Categoria", fontSubtitulo));
            tabelaDespesas.addCell(criarCelula("Valor", fontSubtitulo));

            List<Despesa> despesas = despesaService.listarTodas(); // Buscar despesas do banco
            BigDecimal totalDespesas = BigDecimal.ZERO;
            for (Despesa despesa : despesas) {
                tabelaDespesas.addCell(despesa.getData().format(formatter));
                tabelaDespesas.addCell(despesa.getDescricao());
                tabelaDespesas.addCell(despesa.getCategoria() != null ? despesa.getCategoria().getNome() : "N/A");
                tabelaDespesas.addCell("R$ " + despesa.getValor());
                totalDespesas = totalDespesas.add(despesa.getValor());
            }
            document.add(tabelaDespesas);

            // Total de Despesas
            Paragraph totalDespesasP = new Paragraph(String.format("Total de Despesas: R$ %.2f", totalDespesas), fontTexto);
            totalDespesasP.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalDespesasP);

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
