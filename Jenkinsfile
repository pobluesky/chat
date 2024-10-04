pipeline {
    agent any

    environment {
        GIT_CREDENTIALS_ID = 'github-token'
        AWS_CREDENTIALS_ID = 'aws-token'
        REPO_URL = 'https://github.com/pobluesky/chat.git'
        BRANCH_NAME = 'main'
        ECR_URI = '014498623207.dkr.ecr.ap-northeast-2.amazonaws.com/chat'
        IMAGE_TAG = "chat:${env.BUILD_ID}"
        LATEST_TAG = "latest"
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: "${BRANCH_NAME}", credentialsId: "${GIT_CREDENTIALS_ID}", url: "${REPO_URL}"
            }
        }

        stage('Set Permissions') {
                    steps {
                        sh 'chmod +x ./gradlew'
                    }
                }

        stage('Build Jar') {
            steps {
                script {
                    sh './gradlew clean build -x test'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t ${IMAGE_TAG} .'
                }
            }
        }

        stage('Check AWS CLI') {
            steps {
                script {
                    sh 'aws --version'
                }
            }
        }

        stage('Login to AWS ECR') {
            steps {
                script {
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: "${AWS_CREDENTIALS_ID}"]]) {
                        sh '''
                        aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin ${ECR_URI}
                        '''
                    }
                }
            }
        }

        stage('Push to ECR') {
            steps {
                script {
                    sh '''
                    docker tag $IMAGE_TAG $ECR_URI:$BUILD_ID
                    docker tag $IMAGE_TAG $ECR_URI:$LATEST_TAG
                    docker push $ECR_URI:$BUILD_ID
                    docker push $ECR_URI:$LATEST_TAG
                    '''
                }
            }
        }
    }
}