
<img width="1977" height="896" alt="bitirme" src="https://github.com/user-attachments/assets/bcc9336a-92b4-4309-b204-2721c7d8cc26" />


# ISTE Student System - Dining Hall Management

## Abstract

This study presents digital solutions to common problems in university dining halls, such as queues and the requirement to carry a physical card. A system has been designed that allows students to make online appointments for specific time slots and enter using a QR code instead of a physical card. The goal of this developed system is to increase student satisfaction, reduce time loss, and enable dining hall staff to work in a more planned manner.

**Keywords:** Dining Hall Queues, QR Code, Appointment System, Student Satisfaction, Spring Boot

***

## 1. Introduction

University dining halls often lead to time loss and dissatisfaction for students due to long queues, especially during peak hours. This situation negatively affects both the daily schedules of students and the workload and working hours of the dining hall staff. Another common problem students face is forgetting or losing their dining hall cards. Students who do not have their cards with them cannot access the meal service, which further increases dissatisfaction.

***

## 2. Problem Definition

The main problems experienced in dining halls can be listed as follows:
* **Time Loss:** Students spend a lot of time waiting for meals due to long queues, especially at certain hours.
* **Card Issues:** Students are unable to use their meal rights because they forget or lose their physical cards.
* **Lack of Planning:** Overproduction or underproduction of food can occur because the number of students who will eat daily cannot be predicted.
* **Staff Workload and Hour Problems:** Insufficient numbers of staff during peak hours lead to an increased workload and a decrease in service quality.

***

## 3. Method

The developed system consists of six main components.

### System Components

1.  **Appointment System:** Students can book appointments for specific time slots of the day via a mobile/web interface. Each time slot is controlled by a capacity limit to prevent queue formation.
2.  **Feedback Panel:** Students can provide feedback by rating the meals. This allows for monitoring service quality and making improvements.
3.  **QR Code Entry:** Dining hall entry control is performed without the need for a physical card, thanks to a unique QR code generated for each student.
4.  **Messaging Panel:** A WebSocket-based messaging system was developed to enable real-time communication among students.
5.  **Game Panel:** To increase the frequency of use of the dining hall system and to provide an entertaining experience during waiting times, a game of guessing the object drawn by the opponent was implemented using WebSocket.
6.  **Staff Panel:** Dining hall staff can see the expected number of students for each time slot, allowing for more planned food production and a more balanced distribution of staff workload.

### Technologies Used

* **Backend:** Spring Boot
* **Frontend:** HTML, CSS, JavaScript
* **Database:** PostgreSQL
* **API:** REST API
* **Real-time Communication:** WebSocket
* **Security:** Spring Security (Form Login, Matcher), JSON Web Token (JWT)

### System Architecture

* **Backend & Frontend:** The project's backend was developed with Spring Boot, while the frontend was designed using HTML, CSS, and JavaScript. PostgreSQL was chosen for data storage. User registration, login, appointment booking, and meal evaluation processes are handled via a REST API.
* **Authentication & Authorization:** Form login mechanism is used for user authentication, and authorization processes are managed with Spring Security Matcher. For QR code authentication and authorization, the JSON Web Token (JWT) mechanism was preferred.
* **QR Code Generation:** When a user requests a QR code, a request is sent to the backend. In the service layer, a JWT with a 1-minute validity period is created using the HS512 algorithm, converted into a QR code, and sent to the user. This QR code is then scanned by a staff member or a turnstile, and its validity status is transmitted to the admin panel.
* **Messaging:** When a user selects someone to message, a WebSocket connection is established on the client side, subscribing to the `/topic/messages` channel. Although all messages are broadcast on this general channel, only the messages between the session user and the selected target user are filtered by sender and receiver IDs and displayed on the interface.
* **Game:** When a user joins the game, a socket connection is established, and the user subscribes to three channels: `/topic/word` (sends a private word to the drawer), `/topic/draw` (transmits drawing coordinates in real-time), and `/topic/currentDrawer` (broadcasts changes in the current drawer). The game starts when multiple users join. In each round, one user becomes the drawer and receives a special word. As the drawer draws on the canvas, the data is transmitted in real-time to other players, who can then submit their guesses.

***

## 4. Conclusion & Security Considerations

Logical relationships were established between the modules in the developed system to enhance operational consistency. For example, QR code generation is prevented and invalidated without an appointment. Similarly, users without an appointment are not allowed to provide feedback on meals at the system level.

* **Security Vulnerabilities:**
    * **Game:** Although the word selection is done by the backend, the word is sent to all users via the public `/topic/word` channel. This public broadcast of what should be private information increases the risk of unauthorized access. It is recommended to send this information through user-specific channels.
    * **Messaging:** Messaging operations are broadcast openly to all clients via the `/topic/messages` channel. The differentiation of messages is handled by client-side JavaScript filtering. This approach is vulnerable to client-side manipulation, creating a serious security flaw and compromising data privacy. To enhance security, user-specific messaging channels should be defined.

* **Security Measures:**
    * **QR Code:** The validity period of the authentication token used for QR code entry is limited to 1 minute. After this period, the system automatically generates a new token. This structure minimizes the risk of misuse in case of a QR code leak or theft, as it is only valid for the minute it was generated.

***

## 5. Future Work

* **Digital Wallet:** A balance-controlled digital card system is proposed. A unique digital dining hall card will be defined for each student, and QR code generation will only be possible for users with a sufficient balance. When a QR code is created, the cost of the meal service will be automatically deducted, combining payment and access control.
* **Unauthorized Access Detection:** By integrating sensors and thermal cameras, it will be possible to detect students who take meals without scanning a QR code at a basic level and low cost, with instant notifications sent to staff.
* **Game Moderation:** A "report" option for drawings will be added to the game section. This will allow users to report and have drawings containing profanity, insults, or inappropriate content reviewed.
* **Extended Operational Period:** The system is planned to be expanded to operate on a 7-day cycle instead of just 1 day. The system will automatically reset itself at the end of each day, archiving appointment and comment data from the previous day and clearing it from the active database.
