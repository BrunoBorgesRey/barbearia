package web.controlevacinacao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxLocation;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.filter.PessoaFilter;
import web.controlevacinacao.model.Pessoa;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.PessoaRepository;
import web.controlevacinacao.service.PessoaService;

@Controller
@RequestMapping("/pessoas")
public class PessoaController {

    private static final Logger logger = LoggerFactory.getLogger(PessoaController.class);

    private PessoaService pessoaService;
    private PessoaRepository pessoaRepository;

     public PessoaController(PessoaRepository pessoaRepository, PessoaService pessoaService) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaService = pessoaService;
    }

    @GetMapping("/nova")
    public String abrirCadastroPessoa(Pessoa pessoa) {
        return "pessoas/nova";
    }

    @HxRequest
    @GetMapping("/nova")
    public String abrirCadastroPessoaHTMX(Pessoa pessoa) {
        return "pessoas/nova :: formulario";
    }

    @PostMapping("/nova")
    public String cadastrarPessoa(Pessoa pessoa) {
        pessoaService.salvar(pessoa);
        return "redirect:/pessoas/sucesso";
    }

    @GetMapping("/sucesso")
    public String abrirMensagemSucesso(Model model) {
        model.addAttribute("mensagem", "Pessoa cadastrada com sucesso");
        return "mensagem";
    }

    @HxRequest
    @PostMapping("/nova")
    public String cadastrarPessoaHTMX(@Valid Pessoa pessoa, BindingResult result, HtmxResponse.Builder htmxResponse) {
        if (result.hasErrors()) {
            logger.info("A pessoa recebida para cadastrar não é válida.");
            logger.info("Erros encontrados:");
            for (FieldError erro : result.getFieldErrors()) {
                logger.info("{}", erro);
            }
            return "pessoas/nova :: formulario";
        } else {
            pessoaService.salvar(pessoa);
            HtmxLocation hl = new HtmxLocation("/pessoas/sucesso");
            hl.setTarget("#main");
            hl.setSwap("outerHTML");
            htmxResponse.location(hl);
            return "mensagem";
        }
    }

    @HxRequest
    @GetMapping("/sucesso")
    public String abrirMensagemSucessoHTMX(Model model) {
        model.addAttribute("mensagem", "Pessoa cadastrada com sucesso");
        return "mensagem :: texto";
    }

    @GetMapping("/abrirpesquisar")
    public String abrirPaginaPesquisa() {
        return "pessoas/pesquisar";
    }

    @HxRequest
    @GetMapping("/abrirpesquisar")
    public String abrirPaginaPesquisaHTMX() {
        return "pessoas/pesquisar :: formulario";
    }

    @GetMapping("/pesquisar")
    public String pesquisar(PessoaFilter filtro, Model model,
            @PageableDefault(size = 7) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Pessoa> pagina = pessoaRepository.pesquisar(filtro, pageable);
        logger.info("Pessoas pesquisadas: {}", pagina);
        PageWrapper<Pessoa> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "pessoas/pessoas";
    }

    @HxRequest
    @GetMapping("/pesquisar")
    public String pesquisarHTMX(PessoaFilter filtro, Model model,
            @PageableDefault(size = 7) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Pessoa> pagina = pessoaRepository.pesquisar(filtro, pageable);
        logger.info("Pessoas pesquisadas: {}", pagina);
        PageWrapper<Pessoa> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "pessoas/pessoas :: tabela";
    }

    @PostMapping("/abriralterar")
    public String abrirAlterar(Pessoa pessoa) {
        return "pessoas/alterar";
    }

    @HxRequest
    @PostMapping("/abriralterar")
    public String abrirAlterarHTMX(Pessoa pessoa) {
        return "pessoas/alterar :: formulario";
    }

    @PostMapping("/alterar")
    public String alterar(Pessoa pessoa) {
        pessoaService.alterar(pessoa);
        return "redirect:/pessoas/sucesso2";
    }

    @GetMapping("/sucesso2")
    public String abrirMensagemSucesso2(Model model) {
        model.addAttribute("mensagem", "Pessoa alterada com sucesso");
        return "mensagem";
    }

    @HxRequest
    @PostMapping("/alterar")
    public String alterarHTMX(@Valid Pessoa pessoa, BindingResult result, HtmxResponse.Builder htmxResponse) {
        if (result.hasErrors()) {
            logger.info("A pessoa recebida para alterar não é válida.");
            logger.info("Erros encontrados:");
            for (FieldError erro : result.getFieldErrors()) {
                logger.info("{}", erro);
            }
            return "pessoas/alterar :: formulario";
        } else {
            pessoaService.alterar(pessoa);
            HtmxLocation hl = new HtmxLocation("/pessoas/sucesso2");
            hl.setTarget("#main");
            hl.setSwap("outerHTML");
            htmxResponse.location(hl);
            return "mensagem";
        }
    }

    @HxRequest
    @GetMapping("/sucesso2")
    public String abrirMensagemSucesso2HTMX(Model model) {
        model.addAttribute("mensagem", "Pessoa alterada com sucesso");
        return "mensagem :: texto";
    }

    @PostMapping("/abrirremover")
    public String abrirRemover(Pessoa pessoa) {
        return "pessoas/confirmarremocao";
    }

    @HxRequest
    @PostMapping("/abrirremover")
    public String abrirRemoverHTMX(Pessoa pessoa) {
        return "pessoas/confirmarremocao :: confirmacao";
    }

    @PostMapping("/remover")
    public String remover(Pessoa pessoa) {
        pessoa.setStatus(Status.INATIVO);
        pessoaService.alterar(pessoa);
        return "redirect:/pessoas/sucesso3";
    }

    @GetMapping("/sucesso3")
    public String abrirMensagemSucesso3(Model model) {
        model.addAttribute("mensagem", "Pessoa removida com sucesso");
        return "mensagem";
    }

    @HxRequest
    @PostMapping("/remover")
    public String removerHTMX(Pessoa pessoa, HtmxResponse.Builder htmxResponse) {
        pessoa.setStatus(Status.INATIVO);
        pessoaService.alterar(pessoa);
        HtmxLocation hl = new HtmxLocation("/pessoas/sucesso3");
            hl.setTarget("#main");
            hl.setSwap("outerHTML");
            htmxResponse.location(hl);
        return "mensagem";
    }

    @HxRequest
    @GetMapping("/sucesso3")
    public String abrirMensagemSucesso3HTMX(Model model) {
        model.addAttribute("mensagem", "Pessoa removida com sucesso");
        return "mensagem :: texto";
    }
}
