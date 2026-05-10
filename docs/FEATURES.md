# Fonctionnalités du Projet E-Commerce Core (MVP)

Ce document détaille les fonctionnalités implémentées dans le projet **E-Commerce Core**, un MVP Spring Boot conçu pour simuler un système de gestion e-commerce complet sans persistance SQL.

## 1. Gestion des Entités Core (CRUD)

Le système offre des opérations **CRUD (Create, Read, Update, Delete)** complètes pour les entités fondamentales :

*   **Produits**:
    *   Création, lecture, mise à jour et suppression de produits.
    *   Chaque produit peut avoir plusieurs images.
    *   Gestion des variantes (ex: couleur, taille) avec ajustement de prix.
    *   Système de commentaires et de notes pour chaque produit.
*   **Catégories**:
    *   Création, lecture, mise à jour et suppression de catégories de produits.
*   **Utilisateurs**:
    *   Création, lecture, mise à jour et suppression d'utilisateurs.
    *   Les utilisateurs peuvent avoir des rôles (ex: `ADMIN`, `CUSTOMER`).

## 2. Logique Produit Avancée

*   **Multi-images par Produit**: Chaque produit peut être associé à une liste d'URLs d'images, permettant une présentation visuelle riche.
*   **Gestion des Variantes**: Les produits peuvent avoir des variantes définies par un type (ex: 
Couleur, Taille) et une valeur (ex: Rouge, XL), avec un impact optionnel sur le prix.
*   **Système de Commentaires/Notes**: Les utilisateurs peuvent laisser des commentaires et attribuer une note (de 1 à 5 étoiles) aux produits. Chaque commentaire est horodaté.

## 3. Gestion des Transactions

*   **Panier (Session-based)**: Bien que non explicitement implémenté comme une entité persistante dans ce MVP, la logique de commande est conçue pour supporter un panier basé sur la session où les articles sont collectés avant la finalisation de la commande.
*   **Commandes**: 
    *   Création de commandes avec une liste d'articles, un utilisateur associé, un montant total calculé et une adresse de livraison.
    *   Mise à jour du statut de la commande (PENDING, PAID, SHIPPED, DELIVERED).
    *   Décrémentation automatique du stock des produits lors de la création d'une commande.
*   **Paiements Fictifs**: Un mécanisme simple de 
paiement est simulé, permettant de faire passer une commande du statut `PENDING` à `PAID`.
*   **Promotions**: Supporte l'application de codes promotionnels avec un pourcentage de réduction sur le montant total de la commande. Les promotions peuvent être activées ou désactivées.

## 4. Logistique et Service Client

*   **Expédition**: Le statut d'expédition des commandes peut être mis à jour (`SHIPPED`, `DELIVERED`), simulant le suivi logistique.
*   **Service Client (Ticketing simple)**: Un système de tickets permet aux utilisateurs de soumettre des requêtes ou des problèmes. Chaque ticket a un sujet, un message, un utilisateur associé et un statut (`OPEN`, `CLOSED`).

## 5. Fonctionnalités REST

*   **Pagination et Filtrage Personnalisés**: Les endpoints de récupération de listes (ex: produits) supportent la pagination (`page`, `size`) et le filtrage par critères (ex: `category`, `minPrice`, `maxPrice` pour les produits). Ces opérations sont implémentées directement sur les structures de données en mémoire.
*   **Validation des Données**: L'intégration de **Bean Validation (JSR 380)** assure la validation des données entrantes pour les requêtes `POST` et `PUT`. Des annotations comme `@NotBlank`, `@NotNull`, et `@Min` sont utilisées sur les DTOs pour garantir l'intégrité des données avant le traitement métier.

## 6. Absence de Spring Security

Conformément aux exigences, **Spring Security n'est pas implémenté**. L'application se concentre sur les fonctionnalités core de l'e-commerce sans gérer l'authentification et l'autorisation de manière sécurisée, ce qui est typique pour un MVP axé sur la logique métier pure.
