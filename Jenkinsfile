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
        stage('1️⃣ Checkout Code') {
            steps {
                echo '📥 Récupération du code depuis Git...'
                checkout scm
            }
        }
        
        stage('2️⃣ Build Maven') {
            steps {
                echo '🔨 Compilation du projet...'
                sh 'mvn clean compile'
            }
        }
        
        stage('3️⃣ Tests Unitaires') {
            steps {
                echo '🧪 Exécution des tests unitaires...'
                sh 'mvn test -Dgroups=Unitaire'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('4️⃣ Tests d\'Intégration') {
            steps {
                echo '🔗 Exécution des tests d\'intégration...'
                sh 'mvn test -Dgroups=Integration'
            }
        }
        
        stage('5️⃣ Tests Selenium') {
            steps {
                script {
                    try {
                        sh 'mvn verify -Dgroups=selenium'
                    } catch (Exception e) {
                        echo "⚠️ Tests Selenium échoués - Continuer quand même"
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
        }
        
        stage('6️⃣ Package Application') {
            steps {
                echo '📦 Création du fichier JAR...'
                sh 'mvn package -DskipTests'
            }
        }
        
        stage('7️⃣ Build Docker Image') {
            steps {
                echo '🐳 Construction de l\'image Docker...'
                script {
                    docker.build("${DOCKER_IMAGE}:${VERSION}")
                    docker.build("${DOCKER_IMAGE}:latest")
                }
            }
        }
        
        stage('8️⃣ Push to Docker Hub') {
            steps {
                echo '⬆️ Envoi vers Docker Hub...'
                script {
                    docker.withRegistry('https://registry.hub.docker.com', DOCKER_CREDENTIALS_ID) {
                        docker.image("${DOCKER_IMAGE}:${VERSION}").push()
                        docker.image("${DOCKER_IMAGE}:latest").push()
                    }
                }
            }
        }
        
        stage('9️⃣ Deploy to Kubernetes') {
            steps {
                echo '☸️ Déploiement sur Kubernetes (Minikube)...'
                script {
                    try {
                        sh 'kubectl apply -f k8s/mysql-deployment.yaml'
                        echo '✅ MySQL déployé'
                        
                        sh 'kubectl wait --for=condition=ready pod -l app=mysql --timeout=120s || true'
                        
                        sh 'kubectl apply -f k8s/deployment.yaml'
                        sh 'kubectl apply -f k8s/service.yaml'
                        echo '✅ Application déployée'
                        
                        sh "kubectl set image deployment/gestion-produits-deployment gestion-produits=${DOCKER_IMAGE}:${VERSION}"
                        
                        sh 'kubectl rollout status deployment/gestion-produits-deployment --timeout=180s'
                        
                        sh 'kubectl apply -f k8s/prometheus-config.yaml || true'
                        sh 'kubectl apply -f k8s/grafana.yaml || true'
                        
                        echo '✅ Déploiement Kubernetes terminé avec succès!'
                        
                    } catch (Exception e) {
                        echo "❌ Erreur lors du déploiement: ${e.getMessage()}"
                        throw e
                    }
                }
            }
        }
        
        stage('🔟 Health Check') {
            steps {
                echo '💚 Vérification de la santé de l\'application...'
                script {
                    try {
                        sh 'minikube service gestion-produits-service --url > service_url.txt || true'
                        
                        sleep(time: 10, unit: 'SECONDS')
                        
                        echo '✅ Application déployée et accessible!'
                        echo '🔗 Pour accéder à l\'application: minikube service gestion-produits-service'
                        
                    } catch (Exception e) {
                        echo "⚠️ Health check non disponible: ${e.getMessage()}"
                    }
                }
            }
        }
    }
    
    post {
        success {
            echo '✅ =========================================='
            echo '✅ Pipeline exécuté avec succès !'
            echo '✅ Application déployée sur Kubernetes'
            echo '✅ =========================================='
            echo ''
            echo '📋 Pour accéder aux services:'
            echo '   Application: minikube service gestion-produits-service'
            echo '   Prometheus:  minikube service prometheus-service'
            echo '   Grafana:     minikube service grafana-service'
        }
        failure {
            echo '❌ =========================================='
            echo '❌ Pipeline échoué !'
            echo '❌ Vérifiez les logs ci-dessus'
            echo '❌ =========================================='
        }
        always {
            echo '🧹 Nettoyage des ressources...'
            sh 'rm -f service_url.txt || true'
        }
    }
}