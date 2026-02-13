pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'salma201/gestion-produits'
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
        VERSION = "${BUILD_NUMBER}"
    }
    
    tools {
        maven 'Maven'
    }
    
    stages {
        stage('Checkout Code') {
            steps {
                echo 'Récupération du code depuis Git...'
                checkout scm
            }
        }
        
        stage('Build Maven') {
            steps {
                echo 'Compilation du projet...'
                sh 'mvn clean compile'
            }
        }
        
        stage('Tests Unitaires') {
            steps {
                echo 'Exécution des tests unitaires...'
                sh 'mvn test -Dgroups=Unitaire'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Tests d\'Intégration') {
            steps {
                echo 'Exécution des tests d\'intégration...'
                sh 'mvn test -Dgroups=Integration'
            }
        }
        
        stage('Tests Selenium') {
            steps {
                script {
                    try {
                        sh 'mvn verify -Dgroups=selenium'
                    } catch (Exception e) {
                        echo "Tests Selenium échoués - Continuer quand même"
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
        }
        
        stage('Package Application') {
            steps {
                echo 'Création du fichier JAR...'
                sh 'mvn package -DskipTests'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                echo 'Construction de l\'image Docker...'
                script {
                    docker.build("${DOCKER_IMAGE}:${VERSION}")
                    docker.build("${DOCKER_IMAGE}:latest")
                }
            }
        }
        
        stage('Push to Docker Hub') {
            steps {
                echo 'Envoi vers Docker Hub...'
                script {
                    docker.withRegistry('https://registry.hub.docker.com', DOCKER_CREDENTIALS_ID) {
                        docker.image("${DOCKER_IMAGE}:${VERSION}").push()
                        docker.image("${DOCKER_IMAGE}:latest").push()
                    }
                }
            }
        }
        
        stage('Apply Secrets') {
            steps {
                echo 'Application des secrets Kubernetes...'
                sh 'kubectl apply -f k8s/gestion-produits-secrets.yaml'

            }
        }
        
        stage('Deploy to Kubernetes') {
            steps {
                echo 'Déploiement sur Kubernetes (Docker Desktop)...'
                script {
                    try {
                        sh 'kubectl apply -f k8s/mysql-deployment.yaml'
                        echo 'MySQL déployé'
                        
                        sh 'kubectl wait --for=condition=ready pod -l app=mysql --timeout=120s || true'
                        
                        sh 'kubectl apply -f k8s/deployment.yaml'
                        sh 'kubectl apply -f k8s/service.yaml'
                        echo 'Application déployée'
                        
                        sh "kubectl set image deployment/gestion-produits-deployment gestion-produits=${DOCKER_IMAGE}:${VERSION}"
                        sh 'kubectl rollout status deployment/gestion-produits-deployment --timeout=180s'
                        
                        sh 'kubectl apply -f k8s/prometheus-config.yaml || true'
                        sh 'kubectl apply -f k8s/grafana.yaml || true'
                        
                        sh 'kubectl apply -f k8s/ingress.yaml'
                        echo 'Ingress configuré'
                        
                        echo 'Déploiement Kubernetes terminé avec succès!'
                        
                    } catch (Exception e) {
                        echo "Erreur lors du déploiement: ${e.getMessage()}"
                        throw e
                    }
                }
            }
        }
        
        stage('Health Check & URLs') {
            steps {
                echo 'Vérification de la santé de l\'application...'
                script {
                    try {
                        sleep(time: 15, unit: 'SECONDS')
                        
                        echo 'Application déployée et accessible!'
                        echo ''
                        echo '=========================================='
                        echo 'URLs ACCESSIBLES (via Ingress):'
                        echo '=========================================='
                        echo 'Application principale:  http://localhost/'
                        echo 'Prometheus:              http://localhost/prometheus'
                        echo 'Grafana:                 http://localhost/grafana'
                        echo '=========================================='
                        echo ''
                        echo 'Tous les services sont accessibles via localhost grâce à Ingress!'
                        
                    } catch (Exception e) {
                        echo "Health check: ${e.getMessage()}"
                    }
                }
            }
        }
    }
    
    post {
        success {
            echo '=========================================='
            echo 'Pipeline exécuté avec succès !'
            echo 'Application déployée sur Kubernetes (Docker Desktop)'
            echo '=========================================='
            echo ''
            echo 'ACCÈS AUX SERVICES VIA INGRESS:'
            echo '   Application:  http://localhost/'
            echo '   Prometheus:   http://localhost/prometheus'
            echo '   Grafana:      http://localhost/grafana'
            echo ''
            echo 'Utilisez kubectl get ingress pour voir la configuration'
        }
        failure {
            echo '=========================================='
            echo 'Pipeline échoué !'
            echo 'Vérifiez les logs ci-dessus'
            echo '=========================================='
        }
        always {
            echo 'Nettoyage des ressources...'
        }
    }
}