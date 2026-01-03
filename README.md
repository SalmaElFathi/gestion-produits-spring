# Gestion Produits

## Description

Projet Spring Boot pour gérer des produits avec interface web, base de données et tests automatisés.
Le projet inclut :

* Tests unitaires
* Tests d'intégration
* Tests Selenium end-to-end
* Documentation API via Swagger

Technologies utilisées :

* Java 17
* Spring Boot 3.x
* Spring Data JPA
* Thymeleaf
* MySQL / H2
* Selenium WebDriver
* JUnit 5 et Mockito
* Swagger (springdoc-openapi)

---

## Prérequis

* Java 17 installé
* Maven 3.8+
* Navigateur Chrome pour Selenium
* MySQL (si utilisation en production)

---

## Installation et configuration

1. Cloner le projet :

```bash
git clone <url-du-projet>
cd gestion-produits
```

2. Configurer la base de données :

* En développement : H2 (par défaut pour les tests)
* En production : MySQL

---

## Types de tests

Le projet contient **3 types de tests** :

### 1. Tests unitaires

* Testent la logique métier avec **JUnit 5** et **Mockito**.
* Lancement Maven :

```bash
mvn test -Dgroups=Unitaire
```

### 2. Tests d'intégration

* Testent la collaboration entre les couches avec **H2 en mémoire**.
* Lancement Maven :

```bash
mvn test -Dgroups=Integration
```

### 3. Tests Selenium

* Testent l'application end-to-end avec le navigateur Chrome.
* Lancement Maven :

```bash
mvn test -Dgroups=selenium
```

---

## Commandes Maven utiles

* Lancer tous les tests :

```bash
mvn test
```

* L'application sera accessible sur : **http://localhost:5000**

---

## Documentation API (Swagger)

Une fois l'application lancée, accéder à la documentation Swagger :

**http://localhost:5000/swagger-ui.html**

---

## Dépendances importantes

* **Spring Boot Starter Web** : serveur web
* **Spring Boot Starter Thymeleaf** : template engine
* **Spring Boot Starter Data JPA** : accès base de données
* **H2 Database** : base en mémoire pour tests
* **MySQL Connector** : pour MySQL en prod
* **Spring Boot DevTools** : reload automatique en dev
* **JUnit 5** : tests unitaires
* **Mockito** : mocks pour tests unitaires
* **Selenium Java + WebDriverManager** : tests end-to-end
* **Springdoc OpenAPI** : Swagger UI pour documenter l'API

---

## Notes importantes

* Selenium nécessite que le **navigateur Chrome et le driver soient à jour**.
* H2 est utilisé pour éviter d'altérer la base MySQL en développement et tests.
* Le port par défaut est **5000**
