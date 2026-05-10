# Architecture du Projet E-Commerce Core (MVP)

## 1. Introduction

Ce document décrit l'architecture du projet **E-Commerce Core**, un MVP (Minimum Viable Product) développé avec Spring Boot. L'objectif principal est de fournir une base solide pour une application e-commerce, en mettant l'accent sur une approche **API First** et une **persistance en mémoire** pour simuler un environnement sans base de données relationnelle.

## 2. Principes Architecturaux Clés

*   **API First**: Toutes les interactions avec le système se font via des API RESTful bien définies. Cela garantit une séparation claire entre le frontend et le backend, facilitant le développement parallèle et l'intégration avec diverses interfaces clientes (web, mobile, etc.).
*   **Persistance en Mémoire**: Pour répondre à la contrainte de l'absence de base de données SQL, la persistance des données est gérée entièrement en mémoire à l'aide de `ConcurrentHashMap` et `ArrayList`. Chaque 
Repository utilise ces structures pour simuler les opérations CRUD.
*   **Modularité**: Le projet est structuré en couches distinctes (Controller, Service, Repository, Model/DTO) pour une meilleure maintenabilité et évolutivité.
*   **Validation des Données**: Utilisation de Bean Validation (JSR 380) pour assurer l'intégrité des données reçues via les API REST.

## 3. Structure des Dossiers

L'arborescence du projet suit les conventions standard de Spring Boot et Maven, avec des dossiers dédiés pour chaque couche architecturale :

```
ecommerce-core/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── ecommerce/
│       │           └── core/
│       │               ├── controller/       # Gère les requêtes HTTP entrantes et retourne les réponses
│       │               ├── service/          # Contient la logique métier
│       │               ├── repository/       # Gère la persistance des données en mémoire
│       │               ├── model/            # Définit les structures de données (entités)
│       │               ├── dto/              # Data Transfer Objects pour la validation et la communication
│       │               ├── exception/        # Gestion centralisée des exceptions
│       │               └── config/           # Configurations spécifiques à l'application
│       └── resources/      # Fichiers de configuration (application.properties)
├── docs/                 # Documentation du projet (ARCHITECTURE.md, FEATURES.md, TECH_STRENGTHS.md)
├── postman/              # Collection Postman pour tester les API
└── pom.xml               # Fichier de configuration Maven
```

## 4. Composants Clés

### 4.1. Couche Controller

*   **Responsabilité**: Reçoit les requêtes HTTP, délègue la logique métier aux services et retourne les réponses HTTP.
*   **Technologies**: Spring Web (RestController, RequestMapping, GetMapping, PostMapping, etc.).
*   **Validation**: Utilise `@Valid` et les annotations de Bean Validation sur les DTOs pour valider les données entrantes.

### 4.2. Couche Service

*   **Responsabilité**: Contient toute la logique métier de l'application, y compris la gestion des transactions (panier, commandes, paiements), les promotions, la logistique et la logique produit avancée.
*   **Technologies**: Spring Service, injection de dépendances.
*   **Fonctionnalités**: Implémente la pagination et le filtrage sur les collections en mémoire.

### 4.3. Couche Repository

*   **Responsabilité**: Simule la persistance des données en utilisant des structures de données en mémoire (`ConcurrentHashMap` pour le stockage, `AtomicLong` pour la génération d'ID).
*   **Implémentation**: Une classe `InMemoryRepository` générique fournit les opérations CRUD de base, étendue par des Repositories spécifiques à chaque entité (ex: `ProductRepository`, `UserRepository`).
*   **Absence de SQL**: Aucune connexion à une base de données relationnelle n'est établie.

### 4.4. Couche Model/DTO

*   **Model**: Représente les entités métier (ex: `Product`, `User`, `Order`).
*   **DTO (Data Transfer Object)**: Utilisé pour transférer des données entre les couches, souvent avec des annotations de validation (`@NotBlank`, `@NotNull`, `@Min`).
*   **Lombok**: Utilisé pour réduire le code boilerplate (annotations `@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`).

### 4.5. Gestion des Exceptions

*   Un `GlobalExceptionHandler` (`@ControllerAdvice`) est mis en place pour gérer de manière centralisée les exceptions et retourner des réponses d'erreur cohérentes aux clients API.

## 5. Fonctionnalités Spécifiques

*   **Gestion CRUD Complète**: Pour les produits, catégories, utilisateurs.
*   **Logique Produit Avancée**: Supporte les multi-images par produit, la gestion des variantes (couleur, taille) et un système de commentaires/notes.
*   **Gestion des Transactions**: Panier basé sur la session (simulé), commandes, paiements fictifs, promotions.
*   **Logistique**: Fonctionnalités d'expédition et un système de ticketing simple pour le service client.
*   **Pagination et Filtrage**: Implémentés au niveau de la couche Service pour les listes en mémoire.

## 6. Conclusion

Cette architecture fournit un MVP Spring Boot 
robuste et modulaire, capable de gérer les fonctionnalités essentielles d'un e-commerce sans dépendance à une base de données SQL, ce qui le rend idéal pour des démonstrations ou des phases de prototypage rapide.
