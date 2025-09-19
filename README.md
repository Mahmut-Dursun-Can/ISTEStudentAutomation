
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
## ScreenShots
<img width="1919" height="400" alt="Screenshot_1" src="https://github.com/user-attachments/assets/4d8d1015-00a2-48e9-a154-2087a9f0fc92" />


<img width="1919" height="869" alt="Screenshot_6" src="https://github.com/user-attachments/assets/6cbd0933-a6cf-4386-8350-76d41f59f381" />
<img width="1919" height="875" alt="Screenshot_7" src="https://github.com/user-attachments/assets/365fac99-70bf-41cb-b92b-4e068506b23c" />
<img width="1919" height="868" alt="Screenshot_8" src="https://github.com/user-attachments/assets/26a44c02-7a53-4edf-ab06-841d451c2a91" />
<img width="1919" height="870" alt="Screenshot_9" src="https://github.com/user-attachments/assets/d4c2ba13-c753-4ae6-a3c0-3fa7723ae50e" />
<img width="1919" height="873" alt="Screenshot_10" src="https://github.com/user-attachments/assets/33300436-c7e5-480c-9059-aae2a2c32613" />
<img width="1919" height="873" alt="Screenshot_10" src="https://github.com/user-attachments/assets/71cc4c72-7902-4b97-9c2b-6ff16c4130cb" />
<img width="1919" height="874" alt="Screenshot_11" src="https://github.com/user-attachments/assets/a31263ac-9998-41c2-8c52-3473ab2ce30d" />
<img width="1919" height="880" alt="Screenshot_12" src="https://github.com/user-attachments/assets/e45a0c4d-8a59-454f-87ed-8e622fcfbe4b" />
<img width="1919" height="869" alt="Screenshot_13" src="https://github.com/user-attachments/assets/8d02ab72-e5a4-43df-81e8-8ef5ff6f16cf" />
<img width="1919" height="877" alt="Screenshot_15" src="https://github.com/user-attachments/assets/98a77507-c5d1-4c44-984f-b15386831d7b" />
<img width="1919" height="874" alt="Screenshot_16" src="https://github.com/user-attachments/assets/be7470c4-ec4d-49d0-b73a-b4080022c8f0" />
<img width="1919" height="870" alt="Screenshot_17" src="https://github.com/user-attachments/assets/5b489179-92f0-4a49-a7fb-11d95f04d8bc" />
<img width="1919" height="882" alt="Screenshot_5" src="https://github.com/user-attachments/assets/508fbae3-64f4-4212-a4bf-fdafc3390104" />
