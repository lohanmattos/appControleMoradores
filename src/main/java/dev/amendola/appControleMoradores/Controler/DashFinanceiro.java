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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
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
                                      @RequestParam(required = false) Integer ano,
                                      @RequestParam(required = false) String tipo,
                                      Model model) {

        // Busca todas as movimentações financeiras (receitas e despesas)
        List<MovimentacaoDTO> todasMovimentacoes = financeiroService.listarMovimentacoes();
    	
        // Filtrar por mês e ano
        List<MovimentacaoDTO> movimentacoesFiltradas = todasMovimentacoes.stream()
            .filter(mov -> (mes == null || mov.getData().getMonthValue() == mes))
            .filter(mov -> (ano == null || mov.getData().getYear() == ano))
            .filter(mov -> (tipo == null || tipo.isEmpty() || mov.getTipo().equalsIgnoreCase(tipo)))
            .collect(Collectors.toList());

        // Passar dados filtrados para o modelo
        model.addAttribute("movimentacoes", movimentacoesFiltradas);
        model.addAttribute("mes", mes);
        model.addAttribute("ano", ano);
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

        // Lista de anos disponíveis (gerado dinamicamente)
        List<Integer> anosDisponiveis = todasMovimentacoes.stream()
            .map(mov -> mov.getData().getYear())
            .distinct()
            .sorted()
            .collect(Collectors.toList());
        model.addAttribute("anos", anosDisponiveis);

        return "financeiro/dashFinanceiro"; // Nome do arquivo Thymeleaf
    }

    
    @GetMapping("/dashboard/financeiro/pdf")
    public void gerarPdfFinanceiro(@RequestParam(required = false) Integer mes,
                                   @RequestParam(required = false) Integer ano,
                                   HttpServletResponse response) {
        
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", new Locale("pt", "BR"));


        try {
            // Configuração da resposta HTTP
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=relatorio.pdf");

            // Criar documento PDF com margens
            Document document = new Document(PageSize.A4, 36, 36, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
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
            
            // Adicionar o mês e ano no cabeçalho do relatório
            String mesAnoTexto = "Período: ";
            if (mes != null) {
                mesAnoTexto += monthFormatter.format(LocalDate.of(ano != null ? ano : LocalDate.now().getYear(), mes, 1));
            } else {
                mesAnoTexto += "Todos os meses";
            }
            mesAnoTexto += (ano != null) ? " de " + ano : "";
            
            document.add(new Paragraph("\n"));
            document.add(new LineSeparator());          
            document.add(new Paragraph("\n"));

            // Título do Relatório
            Paragraph titulo = new Paragraph("Extrato Financeiro", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph("\n"));
            document.add(new Paragraph(mesAnoTexto, fontTexto));


            // Seção Receitas
            document.add(new Paragraph("Receitas", fontSubtitulo));
            document.add(new Paragraph("\n"));

            PdfPTable tabelaReceitas = new PdfPTable(3);
            tabelaReceitas.setWidthPercentage(100);
            tabelaReceitas.addCell(criarCelula("Data", fontSubtitulo, BaseColor.GRAY, BaseColor.WHITE));
            tabelaReceitas.addCell(criarCelula("Descrição", fontSubtitulo, BaseColor.GRAY, BaseColor.WHITE));
            tabelaReceitas.addCell(criarCelula("Valor", fontSubtitulo, BaseColor.GRAY, BaseColor.WHITE));

            BigDecimal totalReceitas = BigDecimal.ZERO;
            List<Receita> receitas = receitaRepository.findAll().stream()
                    .filter(r -> (mes == null || r.getData().getMonthValue() == mes))
                    .filter(r -> (ano == null || r.getData().getYear() == ano))
                    .toList();

            for (Receita receita : receitas) {
                tabelaReceitas.addCell(receita.getData().format(formatter));
                tabelaReceitas.addCell(receita.getDescricao());
                tabelaReceitas.addCell("R$ " + receita.getValor());
                totalReceitas = totalReceitas.add(receita.getValor());
            }
            
            
            document.add(tabelaReceitas);
            document.add(new Paragraph(String.format("Total de Receitas: R$ %.2f", totalReceitas), fontTexto));

            document.add(new Paragraph("\n"));
            document.add(new LineSeparator());
            document.add(new Paragraph("\n"));

            // Seção Despesas
            document.add(new Paragraph("Despesas", fontSubtitulo));
            document.add(new Paragraph("\n"));

            PdfPTable tabelaDespesas = new PdfPTable(3);
            tabelaDespesas.setWidthPercentage(100);
            tabelaDespesas.addCell(criarCelula("Data", fontSubtitulo, BaseColor.GRAY, BaseColor.WHITE));
            tabelaDespesas.addCell(criarCelula("Descrição", fontSubtitulo, BaseColor.GRAY, BaseColor.WHITE));
            tabelaDespesas.addCell(criarCelula("Valor", fontSubtitulo, BaseColor.GRAY, BaseColor.WHITE));

            BigDecimal totalDespesas = BigDecimal.ZERO;
            List<Despesa> despesas = despesaRepository.findAll().stream()
                    .filter(d -> (mes == null || d.getData().getMonthValue() == mes))
                    .filter(d -> (ano == null || d.getData().getYear() == ano))
                    .toList();

            for (Despesa despesa : despesas) {
                tabelaDespesas.addCell(despesa.getData().format(formatter));
                tabelaDespesas.addCell(despesa.getDescricao());
                tabelaDespesas.addCell("R$ " + despesa.getValor());
                totalDespesas = totalDespesas.add(despesa.getValor());
            }
            
            document.add(tabelaDespesas);
            document.add(new Paragraph(String.format("Total de Despesas: R$ %.2f", totalDespesas), fontTexto));

            // Saldo Final
            document.add(new Paragraph("\n"));
            BigDecimal saldo = totalReceitas.subtract(totalDespesas);
            Paragraph saldoFinal = new Paragraph("Saldo Final: R$ " + saldo, fontSubtitulo);
            saldoFinal.setAlignment(Element.ALIGN_RIGHT);
            document.add(saldoFinal);

            document.add(new Paragraph("\n"));

            // Assinatura com Local e Data
            LocalDate dataAtual = LocalDate.now();
            
            String localEData = config.getCidade() + " - " + config.getEstado() + ", " + dataAtual.format(formatter);

            Paragraph assinatura = new Paragraph(
                    "___________________________________________\n" +
                    config.getSindico().getNome() + "\n" +
                    "Síndico\n" + localEData,
                    fontTexto
            );
            assinatura.setAlignment(Element.ALIGN_CENTER);
            
            document.add(assinatura);

            // Rodapé Fixo
            Footer footer = new Footer("CorujaCondo - Sistema de Gestão de Condomínios. Desenvolvido por Lohan Amendola", fontTexto);
            writer.setPageEvent(footer);

            document.close();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private PdfPCell criarCelula(String conteudo, Font fonte, BaseColor bgColor, BaseColor fontColor) {
        PdfPCell celula = new PdfPCell(new Phrase(conteudo, new Font(fonte.getFamily(), fonte.getSize(), Font.BOLD, fontColor)));
        celula.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula.setPadding(5);
        celula.setBackgroundColor(bgColor);
        return celula;
    }

    // Footer para ser aplicado no rodapé fixo
    class Footer extends PdfPageEventHelper {
        private final String textoRodape;
        private final Font fonte;

        public Footer(String textoRodape, Font fonte) {
            this.textoRodape = textoRodape;
            this.fonte = fonte;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte canvas = writer.getDirectContent();
            Phrase rodape = new Phrase(textoRodape, fonte);
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, rodape,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottomMargin() - 10, 0);
        }
    }


}
