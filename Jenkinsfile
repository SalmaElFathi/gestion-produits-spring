pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'eddah0salma/gestion-produits'
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
        VERSION = "${BUILD_NUMBER}"
        DOCKER_HOST = 'tcp://host.docker.internal:2375'
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
        
        stage('9️⃣ Deploy Info') {
            steps {
                echo '📋 Image Docker prête pour déploiement Kubernetes'
                echo "Image: ${DOCKER_IMAGE}:${VERSION}"
                echo '✅ La personne 4 peut maintenant déployer sur K8s'
            }
        }
        
        stage('🔟 Health Check Simulation') {
            steps {
                echo '💚 Vérification simulée de la santé de l\'application'
                echo '✅ Application prête à être déployée'
            }
        }
    }
    
    post {
        success {
            echo '✅ =========================================='
            echo '✅ Pipeline exécuté avec succès !'
            echo '✅ =========================================='
        }
        failure {
            echo '❌ =========================================='
            echo '❌ Pipeline échoué !'
            echo '❌ Vérifiez les logs ci-dessus'
            echo '❌ =========================================='
        }
        always {
            echo '🧹 Nettoyage des ressources...'
        }
    }
}