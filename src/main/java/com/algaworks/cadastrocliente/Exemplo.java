package com.algaworks.cadastrocliente;

import com.algaworks.cadastrocliente.model.Cliente;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Exemplo {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("Clientes-PU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // Inserindo um registro com persist.
        Cliente cliente = new Cliente();
        cliente.setNome("MVP Systems");
        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.getTransaction().commit();

        // Buscando um registro.
        cliente = entityManager.find(Cliente.class, 1L);
        System.out.println(cliente.getNome());

        // Inserindo um registro com persist.
        // cliente = new Cliente();
        // cliente.setNome("Eletro Silva");
        // entityManager.getTransaction().begin();
        // entityManager.persist(cliente);
        // entityManager.getTransaction().commit();

        // Removendo o registro.
        // cliente = entityManager.find(Cliente.class, 3);
        // entityManager.getTransaction().begin();
        // entityManager.remove(cliente);
        // entityManager.getTransaction().commit();

        // Usando o cache de primeiro nível.
        // cliente = entityManager.find(Cliente.class, 1L);
        // entityManager.clear();
        // Cliente cliente2 = entityManager.find(Cliente.class, 1L);

        // System.out.println("Cliente: " + cliente2.getNome());

        // Atualizando o registro já gerenciado.
        // cliente = entityManager.find(Cliente.class, 1L);
        // entityManager.getTransaction().begin();
        // cliente.setNome(cliente.getNome() + " Alterado");
        // entityManager.getTransaction().commit();

        // Atualizando um objeto (que não nasceu gerenciado) com o merge.
        // cliente = new Cliente();
        // cliente.setId(1L);
        // cliente.setNome("Construtora Medeiros");
        // entityManager.getTransaction().begin();
        // entityManager.merge(cliente);
        // entityManager.getTransaction().commit();

        // Inserindo com o merge.
        cliente = new Cliente();
        cliente.setNome("Construtora Medeiros");
        entityManager.getTransaction().begin();
        entityManager.merge(cliente);
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }
}