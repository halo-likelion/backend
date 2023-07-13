def mainBranch = false

pipeline {
  agent any
  tools {
      jdk 'JDK 17'
    }
//   environment {
//       JAVA_HOME = "${tool 'JDK_17'}/jdk-17"
//       PATH = "${JAVA_HOME}/bin:/opt/gradle/gradle-7.3/bin:$PATH"
//       SLACK_CHANNEL = '#jenkins-notification'
//   }


  stages {

    stages {
        stage('Print JAVA_HOME') {
          steps {
            script {
              println "JAVA_HOME is: ${System.getenv('JAVA_HOME')}"
            }
          }
        }
      }

    stage('Git Checkout') {
      steps {
        checkout scm
        echo 'Git Checkout Success!'
      }
    }

    stage('Test') {
      steps {
        sh 'gradle test'
        echo 'test success'
      }

      /* post {
        failure {
          slackSend (channel: SLACK_CHANNEL, color: '#F01717', message: "Test Failed '[${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
      } */
    }

    stage('Build') {
      steps {
        sh 'gradle clean build --exclude-task test --exclude-task asciidoctor'
        echo 'build success'
      }

      /* post {
        success {
          slackSend (channel: SLACK_CHANNEL, color: '#00FF00', message: "Successful testing and build '[${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }

        failure {
          slackSend (channel: SLACK_CHANNEL, color: '#F01717', message: "Build Failed '[${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
      } */
    }

    stage('Check Branch') {
      when {
        branch 'develop'
      }

      steps {
        script {
          mainBranch = true
        }
      }
    }

    stage('Idle Port Stop') {
      when {
        expression {
          mainBranch
        }
      }


      steps([$class: 'BapSshPromotionPublisherPlugin']) {
        sshPublisher(
          continueOnError: false, failOnError: true,
          publishers: [
             sshPublisherDesc(
              configName: "shoe-auction-reverse-proxy",
              verbose: true,
              transfers: [
                sshTransfer(
                  execCommand: "sh /root/.scripts/stop.sh"
                )
              ]
            )
          ]
        )
      }
    }

    stage('Deploy To Idle Port') {
      when {
        expression {
          mainBranch
        }
      }

      steps([$class: 'BapSshPromotionPublisherPlugin']) {
        sshPublisher(
          continueOnError: false, failOnError: true,
          publishers: [
            sshPublisherDesc(
              configName: "shoe-auction-reverse-proxy",
              verbose: true,
              transfers: [
                sshTransfer(
                  execCommand: "sh /root/.scripts/start.sh"
                )
              ]
            )
          ]
        )
      }
    }

    stage('Check Health And Switch Ports') {
      when {
        expression {
          mainBranch
        }
      }

      steps([$class: 'BapSshPromotionPublisherPlugin']) {
        sshPublisher(
          continueOnError: false, failOnError: true,
          publishers: [
            sshPublisherDesc(
              configName: "shoe-auction-reverse-proxy",
              verbose: true,
              transfers: [
                sshTransfer(
                  execCommand: "sh /root/.scripts/health.sh"
                )
              ]
            )
          ]
        )
      }

      /* post {
        success {
          slackSend (channel: SLACK_CHANNEL, color: '#00FF00', message: "Health check and deployment successful '[${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }

        failure {
          slackSend (channel: SLACK_CHANNEL, color: '#F01717', message: "Health check and deployment failure '[${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
      } */
    }
  }
}