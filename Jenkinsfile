pipeline {
    agent any
    
    tools {
        maven 'Maven'
    }
    
    environment {
        DOCKER_IMAGE = "eddah0salma/gestion-produits"
        DOCKER_TAG = "${BUILD_NUMBER}"
    }
    
    stages {
        stage('1️⃣ Checkout') {
            steps {
                echo '📥 Récupération du code source depuis GitHub...'
                git branch: 'main', 
                    url: 'https://github.com/SalmaElFathi/gestion-produits-spring.git'
            }
        }
        
        stage('2️⃣ Build & Compile') {
            steps {
                echo '🏗️ Compilation du projet Maven...'
                sh 'mvn clean compile'
            }
        }
        
        stage('3️⃣ Tests Unitaires') {
            steps {
                echo '🧪 Exécution des tests unitaires...'
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    echo '📊 Résultats des tests unitaires enregistrés'
                }
            }
        }
        
        stage('4️⃣ Tests d\'Intégration') {
            steps {
                echo '🔗 Exécution des tests d\'intégration...'
                sh 'mvn test -Dgroups=Integration'
            }
        }
        
        stage('5️⃣ Tests End-to-End (Selenium)') {
            steps {
                script {
                    try {
                        echo '🌐 Exécution des tests Selenium...'
                        sh 'mvn verify -Dgroups=selenium'
                    } catch (Exception e) {
                        echo "⚠️ Tests Selenium échoués (Chrome non disponible)"
                        echo "⚠️ Le pipeline continue..."
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
        }
        
        stage('6️⃣ Package JAR') {
            steps {
                echo '📦 Création du fichier JAR...'
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }
        
        stage('7️⃣ Build Docker Image') {
            steps {
                echo '🐳 Construction de l\'image Docker...'
                script {
                    sh """
                        docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                        docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest
                    """
                }
            }
        }
        
        stage('8️⃣ Push to Docker Hub') {
            steps {
                echo '📤 Authentification et push vers Docker Hub...'
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials', 
                    usernameVariable: 'DOCKER_USER', 
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                        docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                        docker push ${DOCKER_IMAGE}:latest
                        echo "✅ Image poussée : ${DOCKER_IMAGE}:${DOCKER_TAG}"
                    """
                }
            }
        }
        
        stage('9️⃣ Deploy Info') {
            steps {
                echo '🚀 =========================================='
                echo '🚀 Informations de déploiement'
                echo '🚀 =========================================='
                echo "📦 Image Docker : ${DOCKER_IMAGE}:${DOCKER_TAG}"
                echo "📦 Image Latest : ${DOCKER_IMAGE}:latest"
                echo '🚀 Prêt pour le déploiement !'
                echo '🚀 =========================================='
            }
        }
        
        stage('🔟 Cleanup') {
            steps {
                echo '🧹 Nettoyage des images locales...'
                sh """
                    docker rmi ${DOCKER_IMAGE}:${DOCKER_TAG} || true
                    docker system prune -f || true
                """
            }
        }
    }
    
    post {
        always {
            echo '🧹 Nettoyage des ressources Jenkins...'
        }
        success {
            echo '✅ =========================================='
            echo '✅ PIPELINE CI/CD RÉUSSI !'
            echo '✅ =========================================='
            echo "✅ Build #${BUILD_NUMBER} terminé avec succès"
            echo "✅ Image disponible : ${DOCKER_IMAGE}:${DOCKER_TAG}"
            echo '✅ =========================================='
        }
        unstable {
            echo '⚠️ =========================================='
            echo '⚠️ Pipeline terminé avec avertissements'
            echo '⚠️ (Probablement tests Selenium)'
            echo '⚠️ Mais l\'application est déployée !'
            echo '⚠️ =========================================='
        }
        failure {
            echo '❌ =========================================='
            echo '❌ PIPELINE ÉCHOUÉ !'
            echo '❌ Vérifiez les logs ci-dessus'
            echo '❌ =========================================='
        }
    }
}