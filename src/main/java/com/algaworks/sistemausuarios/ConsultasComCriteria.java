package com.algaworks.sistemausuarios;

import com.algaworks.sistemausuarios.dto.UsuarioDTO;
import com.algaworks.sistemausuarios.model.Dominio;
import com.algaworks.sistemausuarios.model.Usuario;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.Arrays;
import java.util.List;

public class ConsultasComCriteria {

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Usuarios-PU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        primeiraConsulta(entityManager,criteriaBuilder);
        escolhendoORetorno(entityManager,criteriaBuilder);
        fazendoProjecoes(entityManager,criteriaBuilder);
        passandoParametros(entityManager,criteriaBuilder);
        ordenandoResultados(entityManager,criteriaBuilder);
        paginandoResultados(entityManager,criteriaBuilder);

        entityManager.close();
        entityManagerFactory.close();
    }

    private static void titulo( String tit){
        System.out.println("########################################################");
        System.out.println("   " + tit);
        System.out.println("########################################################");
    }

    private static void _______nota_______( String nota){
        System.out.println( ">>>>>>> " +  nota + " <<<<<<<");
    }

    public static void primeiraConsulta(EntityManager entityManager, CriteriaBuilder criteriaBuilder) {
        
        titulo("CQ - Primeiras Consultas");

        CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
        Root<Usuario> root = criteriaQuery.from(Usuario.class);

        criteriaQuery.select(root);

        TypedQuery<Usuario> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Usuario> lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println(u.getId() + " - " + u.getNome()));

        /*
        String jpql = "select u from Usuario u";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class);
        List<Usuario> lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println(u.getId() + ", " + u.getNome()));
        */
    }

    public static void escolhendoORetorno(EntityManager entityManager, CriteriaBuilder criteriaBuilder) {
        
        titulo("CQ - Escolhendo o Retorno");
        
        _______nota_______("Selecionando somente o DOMÍNIO");
        CriteriaQuery<Dominio> criteriaQuery = criteriaBuilder.createQuery(Dominio.class);
        Root<Usuario> rootUsuario = criteriaQuery.from(Usuario.class);
        criteriaQuery.select(rootUsuario.get("dominio")); //root é usuário

        TypedQuery<Dominio> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Dominio> listaDom = typedQuery.getResultList();
        listaDom.forEach(d -> System.out.println(d.getId() + "-" + d.getNome()));

        _______nota_______("Selecionando somente o NOME");
        CriteriaQuery<String> criteriaQueryString = criteriaBuilder.createQuery(String.class);
        rootUsuario = criteriaQueryString.from(Usuario.class);
        criteriaQueryString.select(rootUsuario.get("nome"));
        TypedQuery<String> typedQueryString = entityManager.createQuery(criteriaQueryString);
        List<String> listaNome = typedQueryString.getResultList();
        listaNome.forEach(nome -> System.out.println("Nome: " + nome));

        /*
        nota("Selecionando o domínio");
        String jpql = "select u.dominio from Usuario u where u.id = 1";
        TypedQuery<Dominio> typedQuery = entityManager.createQuery(jpql, Dominio.class);
        Dominio dominio = typedQuery.getSingleResult();
        System.out.println(dominio.getId() + ", " + dominio.getNome());

        nota("Selecionando o campo nome");
        String jpqlNom = "select u.nome from Usuario u";
        TypedQuery<String> typedQueryNom = entityManager.createQuery(jpqlNom, String.class);
        List<String> listaNom = typedQueryNom.getResultList();
        listaNom.forEach(nome -> System.out.println(nome));
        */

    }

    public static void fazendoProjecoes(EntityManager entityManager, CriteriaBuilder criteriaBuilder) {

        titulo("CQ - Fazendo Projeções");
        _______nota_______("Usando Usuario.class");
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Usuario> root = criteriaQuery.from(Usuario.class);
        criteriaQuery.multiselect(root.get("id"), root.get("login"), root.get("nome"));
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();
        lista.forEach(arr -> System.out.println(String.format("| %s | %-7s | %-30s |", arr)));

        _______nota_______("Usando UsuarioDTO.class");
        CriteriaQuery<UsuarioDTO> criteriaQueryDTO = criteriaBuilder.createQuery(UsuarioDTO.class);
        root = criteriaQueryDTO.from(Usuario.class);
        criteriaQueryDTO.select(criteriaBuilder.construct( UsuarioDTO.class
                                                         , root.get("id")
                                                         , root.get("login")
                                                         , root.get("nome")));

        TypedQuery<UsuarioDTO> typedQueryDTO = entityManager.createQuery(criteriaQueryDTO);
        List<UsuarioDTO> listaDTO = typedQueryDTO.getResultList();
        listaDTO.forEach(u -> System.out.println("DTO: " + u.getId() + " | " + u.getNome()));

        /*
        String jpqlArr = "select id, login, nome from Usuario";
        TypedQuery<Object[]> typedQueryArr = entityManager.createQuery(jpqlArr, Object[].class);
        List<Object[]> listaArr = typedQueryArr.getResultList();
        listaArr.forEach(arr -> System.out.println(String.format("%s | %-20s | %-30s", arr)));


        String jpqlDto = "select new com.algaworks.sistemausuarios.dto.UsuarioDTO(id, login, nome)" +
                "from Usuario";
        TypedQuery<UsuarioDTO> typedQueryDto = entityManager.createQuery(jpqlDto, UsuarioDTO.class);
        List<UsuarioDTO> listaDto = typedQueryDto.getResultList();
        listaDto.forEach(u -> System.out.println("DTO: " + u.getId() + "-" + String.format("%-30s", u.getNome())));
        */
    }

    public static void passandoParametros(EntityManager entityManager, CriteriaBuilder criteriaBuilder) {

        titulo("CQ - Passando Parâmetros");

        CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
        Root<Usuario> root = criteriaQuery.from(Usuario.class);

        _______nota_______("CQ - Passando Parâmetros ID");

        criteriaQuery.select(root);

        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 7));

        TypedQuery<Usuario> typedQuery = entityManager.createQuery(criteriaQuery);
        Usuario usuario = typedQuery.getSingleResult();
        System.out.println(usuario.getId() + "-" + usuario.getNome());

        _______nota_______("CQ - Passando Parâmetros LOGIN");
        criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
        root = criteriaQuery.from(Usuario.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("login"), "ria"));

        typedQuery = entityManager.createQuery(criteriaQuery);
        usuario = typedQuery.getSingleResult();
        System.out.println(usuario.getId() + "-" + usuario.getNome());

        /*
        _______nota_______("Parâmetro ID");
        String jpql = "select u from Usuario u where u.id = :idUsuario";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class)
                                                      .setParameter("idUsuario", 7);
        Usuario usuario = typedQuery.getSingleResult();
        System.out.println(usuario.getId() + " - " + usuario.getNome());

        _______nota_______("Parâmetro LOGIN");
        String jpqlLog = "select u from Usuario u where u.login = :loginUsuario";
        TypedQuery<Usuario> typedQueryLog = entityManager.createQuery(jpqlLog, Usuario.class)
                                                         .setParameter("loginUsuario", "ria");
        Usuario usuarioLog = typedQueryLog.getSingleResult();
        System.out.println(usuarioLog.getId() + " - " + usuarioLog.getNome());
        */
    }

    public static void ordenandoResultados(EntityManager entityManager, CriteriaBuilder criteriaBuilder) {

        titulo("CQ - Ordenando Resultados");

        _______nota_______("Ordenação NOME ascendente");
        CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
        Root<Usuario> root = criteriaQuery.from(Usuario.class);

        criteriaQuery.select(root);
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("nome")));

        TypedQuery<Usuario> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Usuario> lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println("| "  + u.getId() + " | " + String.format("%-30s", u.getNome()) + " | "));

        _______nota_______("Ordenação NOME descendente");
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("nome")));
        typedQuery = entityManager.createQuery(criteriaQuery);
        lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println("| "  + u.getId() + " | " + String.format("%-30s", u.getNome()) + " | "));

        _______nota_______("Ordenação DOMÍNIO asc , NOME desc");
        //criteriaQuery.orderBy(criteriaBuilder.desc(root.get("dominio").get("nome")));
        criteriaQuery.orderBy(Arrays.asList(criteriaBuilder.asc(root.get("dominio").get("nome")),criteriaBuilder.desc(root.get("nome"))));
        typedQuery = entityManager.createQuery(criteriaQuery);
        lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println("| "  + String.format("%-15s", u.getDominio().getNome())+ " | " + u.getId() + " | " + String.format("%-30s", u.getNome()) + " | "));

        /*
        String jpql = "select u from Usuario u order by u.nome";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class);
        List<Usuario> lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println("| "  + u.getId() + " | " + String.format("%-30s", u.getNome()) + " | "));
        */

    }

    public static void paginandoResultados(EntityManager entityManager, CriteriaBuilder criteriaBuilder) {
 
        titulo("CQ - Fazendo paginação");
        CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
        Root<Usuario> root = criteriaQuery.from(Usuario.class);

        criteriaQuery.select(root);

        int total_de_registros = entityManager.createQuery(criteriaQuery).getResultList().size();

        final int qtd_por_pagina = 2;

        int total_de_paginas = (total_de_registros/qtd_por_pagina) + (total_de_registros%qtd_por_pagina == 0?0:1);

        List<Usuario> lista;

        for (int i = 0; i < total_de_paginas; i++) {

            lista = entityManager.createQuery(criteriaQuery)
                         .setFirstResult(i*qtd_por_pagina)
                         .setMaxResults(qtd_por_pagina)
                         .getResultList();
            
            System.out.println("------- Página " + (i+1) + "/" + total_de_paginas + " -------");
            lista.forEach(u -> System.out.println("| "+ u.getId() + " | " + String.format("%-30s", u.getNome())+ " |"));
        }    
        
        /*
        String jpql = "select u from Usuario u";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class)
                .setFirstResult(0)
                .setMaxResults(2);
        List<Usuario> lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println(u.getId() + ", " + u.getNome()));
        */
    }

}