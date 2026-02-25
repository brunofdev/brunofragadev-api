package com.brunofragadev.projetos.mapper;

import com.brunofragadev.projetos.dto.*;
import com.brunofragadev.projetos.entitys.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjetoMapper {

    public Projeto toEntity(ProjetoRequestDTO dto, Projeto projeto) {
        projeto.setTitle(dto.title());
        projeto.setDescription(dto.description());
        projeto.setVideo(dto.video());
        projeto.setStatus(dto.status());
        projeto.setPapel(dto.papel());
        projeto.setDataProjeto(dto.dataProjeto());
        projeto.setRepositorioUrl(dto.repositorioUrl());
        projeto.setLiveUrl(dto.liveUrl());

        // Mapeando Ficha Técnica
        if (dto.techs() != null) {
            FichaTecnica ficha = new FichaTecnica();
            ficha.setLinguagem(dto.techs().linguagem());
            ficha.setParadigma(dto.techs().paradigma());
            ficha.setFramework(dto.techs().framework());
            ficha.setBibliotecas(dto.techs().bibliotecas());
            ficha.setInfraestrutura(dto.techs().infraestrutura());
            projeto.setTechs(ficha);
        }

        // Mapeando Setup e Passos
        if (dto.setup() != null) {
            SetupProjeto setup = new SetupProjeto();
            setup.setObs(dto.setup().obs());
            if (dto.setup().steps() != null) {
                List<PassoSetup> passos = dto.setup().steps().stream().map(p -> {
                    PassoSetup passo = new PassoSetup();
                    passo.setNum(p.num());
                    passo.setText(p.text());
                    passo.setCmd(p.cmd());
                    return passo;
                }).collect(Collectors.toList());
                setup.setSteps(passos);
            }
            projeto.setSetup(setup);
        }

        // Mapeando Galeria
        if (dto.galeria() != null) {
            List<ImagemProjeto> imagens = dto.galeria().stream().map(imgDto -> {
                ImagemProjeto img = new ImagemProjeto();
                img.setUrlImagem(imgDto.urlImagem());
                img.setOrdemExibicao(imgDto.ordemExibicao());
                img.setIsCapa(imgDto.isCapa());
                img.setLegenda(imgDto.legenda());

                return img;
            }).collect(Collectors.toList());
            projeto.setGaleria(imagens);
        }

        return projeto;
    }

    public ProjetoResponseDTO toDTO(Projeto projeto) {
        FichaTecnicaDTO techsDTO = projeto.getTechs() != null ? new FichaTecnicaDTO(
                projeto.getTechs().getLinguagem(), projeto.getTechs().getParadigma(),
                projeto.getTechs().getFramework(), projeto.getTechs().getBibliotecas(),
                projeto.getTechs().getInfraestrutura()
        ) : null;

        SetupProjetoDTO setupDTO = null;
        if (projeto.getSetup() != null) {
            List<PassoSetupDTO> passosDTO = projeto.getSetup().getSteps().stream()
                    .map(p -> new PassoSetupDTO(p.getNum(), p.getText(), p.getCmd()))
                    .collect(Collectors.toList());
            setupDTO = new SetupProjetoDTO(projeto.getSetup().getObs(), passosDTO);
        }

        List<ImagemProjetoDTO> galeriaDTO = projeto.getGaleria().stream()
                .map(img -> new ImagemProjetoDTO(
                        img.getId(),
                        img.getUrlImagem(),
                        img.getOrdemExibicao(),
                        img.getIsCapa(),
                        img.getLegenda()
                ))
                .collect(Collectors.toList());

        return new ProjetoResponseDTO(
                projeto.getId(), projeto.getTitle(), projeto.getDescription(),
                projeto.getVideo(), projeto.getStatus(), projeto.getPapel(),
                projeto.getDataProjeto(), projeto.getRepositorioUrl(), projeto.getLiveUrl(),
                techsDTO, setupDTO, galeriaDTO
        );
    }
}