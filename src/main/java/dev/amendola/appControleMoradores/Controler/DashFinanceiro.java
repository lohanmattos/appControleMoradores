package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.DTO.MovimentacaoDTO;
import dev.amendola.appControleMoradores.Model.ConfigCondominio;
import dev.amendola.appControleMoradores.Model.Despesa;
import dev.amendola.appControleMoradores.Model.Receita;
import dev.amendola.appControleMoradores.Repository.DespesaRepository;
import dev.amendola.appControleMoradores.Repository.ReceitaRepository;
import dev.amendola.appControleMoradores.Service.ConfigCondominioService;
import dev.amendola.appControleMoradores.Service.DashFinanceiroService;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class DashFinanceiro {

    @Autowired
    private DashFinanceiroService financeiroService;
   
    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private DespesaRepository despesaRepository;
    
    @Autowired
    private ConfigCondominioService configCondominioService;

    @GetMapping("/dashFinanceiro")
    public String dashboard(Model model) {
    	model.addAttribute("titulo", "Painel Financeiro");
         
        BigDecimal totalReceitas = financeiroService.calcularTotalReceitas();
        BigDecimal totalDespesas = financeiroService.calcularTotalDespesas();
        BigDecimal lucro = totalReceitas.subtract(totalDespesas);
        
        // Busca todas as movimentações financeiras (receitas e despesas)
        List<MovimentacaoDTO> movimentacoes = financeiroService.listarMovimentacoes();

        model.addAttribute("totalReceitas", String.format("R$ %.2f", totalReceitas));
        model.addAttribute("totalDespesas", String.format("R$ %.2f", totalDespesas));
        model.addAttribute("lucro", String.format("R$ %.2f", lucro));
        
        model.addAttribute("lucroStatus", lucro);

        
        model.addAttribute("movimentacoes", movimentacoes);


        return "financeiro/dashFinanceiro";
    }
    
    @GetMapping("/dashboard/financeiro")
    public String dashboardFinanceiro(@RequestParam(required = false) Integer mes,
                                      @RequestParam(required = false) String tipo,
                                      Model model) {
        // Simular a lista de movimentações (normalmente você buscaria do banco de dados)
        List<MovimentacaoDTO> todasMovimentacoes = List.of(
            new MovimentacaoDTO("Receita", "Venda de Produto", new BigDecimal("1200.00"), LocalDate.of(2024, 1, 15)),
            new MovimentacaoDTO("Despesa", "Compra de Material", new BigDecimal("450.00"), LocalDate.of(2024, 2, 10)),
            new MovimentacaoDTO("Receita", "Consultoria", new BigDecimal("800.00"), LocalDate.of(2024, 3, 5))
        );

        // Filtrar por mês (se o filtro for fornecido)
        List<MovimentacaoDTO> movimentacoesFiltradas = todasMovimentacoes.stream()
            .filter(mov -> (mes == null || mov.getData().getMonthValue() == mes))
            .filter(mov -> (tipo == null || tipo.isEmpty() || mov.getTipo().equalsIgnoreCase(tipo)))
            .collect(Collectors.toList());

        // Passar dados filtrados para o modelo
        model.addAttribute("movimentacoes", movimentacoesFiltradas);
        model.addAttribute("mes", mes);
        model.addAttribute("tipo", tipo);

        // Dados do resumo
        model.addAttribute("totalReceitas", movimentacoesFiltradas.stream()
            .filter(mov -> mov.getTipo().equalsIgnoreCase("Receita"))
            .map(MovimentacaoDTO::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        model.addAttribute("totalDespesas", movimentacoesFiltradas.stream()
            .filter(mov -> mov.getTipo().equalsIgnoreCase("Despesa"))
            .map(MovimentacaoDTO::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        model.addAttribute("lucro", movimentacoesFiltradas.stream()
            .map(mov -> mov.getTipo().equalsIgnoreCase("Receita") ? mov.getValor() : mov.getValor().negate())
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        return "financeiro/dashFinanceiro"; // Nome do arquivo Thymeleaf
    }
    
    @GetMapping("/dashboard/financeiro/pdf")
    public void gerarPdfFinanceiro(HttpServletResponse response) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            // Configurar resposta HTTP
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=relatorio.pdf");

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
            Paragraph titulo = new Paragraph("Extrato Financeiro", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph("\n"));

            // Seção Receitas
        
            Paragraph Receitas = new Paragraph("Receitas", fontSubtitulo);
            Receitas.setAlignment(Element.ALIGN_LEFT);
            document.add(Receitas);
            
            PdfPTable tabelaReceitas = new PdfPTable(3);
            tabelaReceitas.setWidthPercentage(100);
            tabelaReceitas.setSpacingBefore(10f);
            tabelaReceitas.addCell(criarCelula("Data", fontSubtitulo));
            tabelaReceitas.addCell(criarCelula("Descrição", fontSubtitulo));
            tabelaReceitas.addCell(criarCelula("Valor", fontSubtitulo));

            List<Receita> receitas = receitaRepository.findAll();
            
            BigDecimal totalReceitas = BigDecimal.ZERO;
            for (Receita receita : receitas) {
                tabelaReceitas.addCell(receita.getData().format(formatter)); // Format the date
                tabelaReceitas.addCell(receita.getDescricao());
                tabelaReceitas.addCell("R$ " + receita.getValor());
                totalReceitas = totalReceitas.add(receita.getValor());
            }
            document.add(tabelaReceitas);
            
            Paragraph totalReceitasP = new Paragraph(String.format("Total de Receitas: R$ %.2f", totalReceitas, fontTexto));
            totalReceitasP.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalReceitasP);

            document.add(new Paragraph("\n"));           
            document.add(new LineSeparator());                                  
            document.add(new Paragraph("\n"));

            // Seção Despesas            
            Paragraph Despesas = new Paragraph("Despesas", fontSubtitulo);
            Despesas.setAlignment(Element.ALIGN_LEFT);
            document.add(Despesas);
            
            
            PdfPTable tabelaDespesas = new PdfPTable(3);
            tabelaDespesas.setWidthPercentage(100);
            tabelaDespesas.setSpacingBefore(10f);
            tabelaDespesas.addCell(criarCelula("Data", fontSubtitulo));
            tabelaDespesas.addCell(criarCelula("Descrição", fontSubtitulo));
            tabelaDespesas.addCell(criarCelula("Valor", fontSubtitulo));

            List<Despesa> despesas = despesaRepository.findAll();
            BigDecimal totalDespesas = BigDecimal.ZERO;
            for (Despesa despesa : despesas) {
                tabelaDespesas.addCell(despesa.getData().format(formatter));
                tabelaDespesas.addCell(despesa.getDescricao());
                tabelaDespesas.addCell("R$ " + despesa.getValor());
                totalDespesas = totalDespesas.add(despesa.getValor());
            }
            document.add(tabelaDespesas);
            
            Paragraph totalDespesasP = new Paragraph(String.format("Total de Despesas: R$ %.2f", totalDespesas, fontTexto));
            totalDespesasP.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalDespesasP);
            

            document.add(new Paragraph("\n"));

            // Saldo Final
            BigDecimal saldo = totalReceitas.subtract(totalDespesas);
            Paragraph saldoFinal = new Paragraph("Saldo Final: R$ " + saldo, fontSubtitulo);
            saldoFinal.setAlignment(Element.ALIGN_RIGHT);
            document.add(saldoFinal);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));


         // Assinatura com Local e Data
            LocalDate dataAtual = LocalDate.now();
            DateTimeFormatter formatterAss = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));

            String localEData = config.getCidade() + " - " + config.getEstado() + ", " + dataAtual.format(formatter);

            Paragraph assinatura = new Paragraph(
                    "___________________________________________\n" +
                    config.getSindico().getNome() + "\n" +
                    "Síndico\n\n" +
                    localEData,
                    fontTexto
            );
            assinatura.setAlignment(Element.ALIGN_LEFT);
            document.add(assinatura);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));


            // Rodapé
            document.add(new LineSeparator());                                  
            Paragraph rodapeT = new Paragraph("CorujaCondo - Sistema de Gestão de Condomínios.", fontTexto);
            rodapeT.setAlignment(Element.ALIGN_BOTTOM);
            document.add(rodapeT);
            Paragraph rodapeD = new Paragraph("Desenvolvido: Lohan Amendola", fontTexto);
            rodapeD.setAlignment(Element.ALIGN_BOTTOM);
            document.add(rodapeD);

            document.close();
        } catch (IOException | DocumentException e) {
            ((Throwable) e).printStackTrace();
        }
    }

    private PdfPCell criarCelula(String conteudo, Font fonte) {
        PdfPCell celula = new PdfPCell(new Phrase(conteudo, fonte));
        celula.setHorizontalAlignment(Element.ALIGN_LEFT);
        celula.setPadding(2);
        // Configurações de estilo para cabeçalhos
        celula.setBackgroundColor(BaseColor.GRAY); // Fundo cinza
        celula.setPhrase(new Phrase(conteudo, new Font(fonte.getFamily(), fonte.getSize(), Font.BOLD, BaseColor.WHITE))); // Texto branco
        return celula;
    }

}
