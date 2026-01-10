# Gestion Produits

## Description
Projet Spring Boot pour gérer des produits avec interface web, base de données et tests automatisés.

**Fonctionnalités** :
* CRUD complet (Créer, Lire, Modifier, Supprimer)
* Interface web avec Thymeleaf
* API REST documentée (Swagger)
* Tests automatisés (Unitaires, Intégration, Selenium)
* Containerisation Docker
* Pipeline CI/CD avec Jenkins
* Déploiement Kubernetes
* Monitoring avec Prometheus et Grafana

**Technologies** :
* Java 17, Spring Boot 3.x, Spring Data JPA
* MySQL (production) / H2 (tests)
* Thymeleaf, Bootstrap
* JUnit 5, Mockito, Selenium WebDriver
* Docker, Kubernetes, Jenkins
* Prometheus, Grafana, Actuator
* Swagger (springdoc-openapi)

---

## Prérequis

* Java 17
* Maven 3.8+
* Docker & Docker Compose
* Chrome (pour tests Selenium)

---

## Installation

### 1. Cloner le projet
```bash
git clone https://github.com/SalmaElFathi/gestion-produits-spring.git
cd gestion-produits
```

### 2. Configurer l'environnement
```bash
cp .env.example .env
# Éditer .env avec vos valeurs
```

### 3. Lancer avec Docker Compose
```bash
docker-compose up --build
```

L'application sera accessible sur : **http://localhost:5000**

---

## Tests

Le projet contient **3 types de tests** :

| Type | Description | Commande |
|------|-------------|----------|
| **Unitaires** | Logique métier (Mockito) | `mvn test -Dgroups=Unitaire` |
| **Intégration** | Couches applicatives (H2) | `mvn test -Dgroups=Integration` |
| **Selenium** | Tests end-to-end (Chrome) | `mvn verify -Dgroups=selenium` |

**Lancer tous les tests** :
```bash
mvn verify
```

---

## Documentation API

**Swagger UI** : http://localhost:5000/swagger-ui.html

**Endpoints Actuator** :
* Health : http://localhost:5000/actuator/health
* Prometheus : http://localhost:5000/actuator/prometheus

---

## CI/CD Pipeline

Le pipeline Jenkins automatise :
1. Build Maven
2. Tests (Unitaires, Intégration, Selenium)
3. Build Docker image
4. Push vers Docker Hub
5. Déploiement Kubernetes
6. Health check

---

## Monitoring

**Prometheus + Grafana** pour surveiller :
* Métriques JVM
* Performances applicatives
* Santé des services

---

## Structure du projet
```
gestion-produits/
├── src/
│   ├── main/java/          # Code source
│   ├── main/resources/     # Configuration, templates
│   └── test/java/          # Tests
├── k8s/                    # Manifests Kubernetes
├── Dockerfile              # Image Docker
├── docker-compose.yml      # Orchestration locale
├── Jenkinsfile             # Pipeline CI/CD
├── .env.example            # Template variables
└── pom.xml                 # Dépendances Maven
```

---

## Notes importantes

* **Port par défaut** : 5000
* **H2** utilisé pour les tests 
* **Chrome** et ChromeDriver gérés automatiquement par WebDriverManager


---
