![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Microservices](https://img.shields.io/badge/Microservices-000000?style=for-the-badge&logo=googlecloud&logoColor=white)


![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![Kafka](https://img.shields.io/badge/Apache_Kafka-000000?style=for-the-badge&logo=apachekafka&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Eureka](https://img.shields.io/badge/Eureka_Server-4B0082?style=for-the-badge)
![API Gateway](https://img.shields.io/badge/API_Gateway-FF6F00?style=for-the-badge)






## ---- Flight Booking Microservices System---

A fully functional microservices-based flight booking system built with **Spring Boot**, **Eureka**, **Kafka**, **Spring Cloud Gateway**, and **Mailtrap SMTP**.

This project handles the full workflow from **searching flights → booking → reducing seats → sending confirmation emails**, just like real-world airline systems.  

---
[![](https://mermaid.ink/img/pako:eNqNlGuPojAUhv9K02SS3cQLFwWGD5uM6GwmuyIZnC-Lk02Fio1ATSlzWfG_LxRYUTFZPpS3Pc85vbyFA_RpgKEJNxF997eIcfDzeZWA4rm7A_OHJxtYi7mzsGf20q3GX1LMvLIZOjTlMUpeq_HviON39Ok9OE-NBoZkSHV4ljG8Q171AoauyXWgah8jEm65--Z7lQIuZm_Ex2WJhpxQuiNJWEK1bFOjZqIYkahkhGgT444ZpxPvCyIsIgn-Hay_ns9UBteVbAWr9gfa7JCLEfeEMkFa6H5J46DP6Z74ry2wLtiwddG-T5MNYTEOzkq786XjzYvFc4b2oleE_5liLWx7Zi2fFnbLENDvf8udhbsEw43Y1xDtSS3TvPGjk6_XIhJqfZlxZnCZerLrOnRyqdPfVvp0culru0AT7WLyR0zCBFgoivLLtXTyTraOSLrNT7b9B1YHO10XuEWTNIvx1RLa2edgczlvHk3-sg-KwxS3Kc0vDqrJFqCLkwDgciQXd-RGyUGBPuOQpByzvP4Kr_d-mzrN2cXAHgwZCaDJWYZ7MMaswIsuPJTZK8i3OMYraBYyQGy3gqvkWOTsUfKL0rhJYzQLt9DcoCgtepk4gSlBIUMnpNgtZhbNEg5NTR2LGtA8wA9o9hVlMBqNdUPRJVVTNFXSe_ATmvK9MVDGumrokibJknLswT9iVnkgy2NV00eje0M1NFlXehAHhFM2r_6G4qd4_AsBJ6A0?type=png)](https://mermaid.live/edit#pako:eNqNlGuPojAUhv9K02SS3cQLFwWGD5uM6GwmuyIZnC-Lk02Fio1ATSlzWfG_LxRYUTFZPpS3Pc85vbyFA_RpgKEJNxF997eIcfDzeZWA4rm7A_OHJxtYi7mzsGf20q3GX1LMvLIZOjTlMUpeq_HviON39Ok9OE-NBoZkSHV4ljG8Q171AoauyXWgah8jEm65--Z7lQIuZm_Ex2WJhpxQuiNJWEK1bFOjZqIYkahkhGgT444ZpxPvCyIsIgn-Hay_ns9UBteVbAWr9gfa7JCLEfeEMkFa6H5J46DP6Z74ry2wLtiwddG-T5MNYTEOzkq786XjzYvFc4b2oleE_5liLWx7Zi2fFnbLENDvf8udhbsEw43Y1xDtSS3TvPGjk6_XIhJqfZlxZnCZerLrOnRyqdPfVvp0culru0AT7WLyR0zCBFgoivLLtXTyTraOSLrNT7b9B1YHO10XuEWTNIvx1RLa2edgczlvHk3-sg-KwxS3Kc0vDqrJFqCLkwDgciQXd-RGyUGBPuOQpByzvP4Kr_d-mzrN2cXAHgwZCaDJWYZ7MMaswIsuPJTZK8i3OMYraBYyQGy3gqvkWOTsUfKL0rhJYzQLt9DcoCgtepk4gSlBIUMnpNgtZhbNEg5NTR2LGtA8wA9o9hVlMBqNdUPRJVVTNFXSe_ATmvK9MVDGumrokibJknLswT9iVnkgy2NV00eje0M1NFlXehAHhFM2r_6G4qd4_AsBJ6A0)

## Deployed on AWS EC2

<img width="1911" height="848" alt="image" src="https://github.com/user-attachments/assets/2922e08f-6a93-4576-9032-d2aca5c3c78c" />


## Started Eureka Service 

<img width="1919" height="1017" alt="image" src="https://github.com/user-attachments/assets/81949777-27ad-4e0b-b765-cd8f5a268287" />


## started docker container on ec2 instance 

<img width="1734" height="924" alt="image" src="https://github.com/user-attachments/assets/15c27e0f-cfba-455a-8887-39eafc033a50" />

##  Architecture Overview

This project contains **5 microservices + Kafka**:

### 1️. Flight Service (8081)
- Manages flights & seat inventory  
- Search flights, get flight by number  
- Listens to Kafka `seat-booked-topic`  
- Reduces seat count dynamically  

### 2️ Booking Service (8084)
- Handles booking requests  
- Uses **OpenFeign** to fetch flight details  
- Publishes:
  - `SeatBookedEvent` → Kafka  
  - `BookingConfirmedEvent` → Kafka  

### 3️ Email Service (8085)
- Listens to `booking-confirmed` topic  
- Sends confirmation email via **Mailtrap SMTP**  

### 4️ Eureka Server (8761)
- Service Registry  
- All services auto-register
  

 EUREKA REGISTERED SERVICES <br>
 
 <img width="1810" height="446" alt="image" src="https://github.com/user-attachments/assets/0d0d7544-ef58-4fd3-b2b2-f2c198e3f4c7" />

## Docker containers running successfully
<img width="1911" height="957" alt="image" src="https://github.com/user-attachments/assets/907173f0-c62d-4284-b3d2-dac14bfd96e2" />


### 5️ API Gateway (9090)
- Routes ALL external requests  
- Uses load-balanced Eureka routing  

### 6️ Kafka (9092)
- Event-driven communication  
- Topics:
  - `seat-booked-topic`
  - `booking-confirmed`

---

##  Event Flow (End-to-End)
 KAFKA FLOW  <br>

 [![](https://mermaid.ink/img/pako:eNp1U9uO2jAQ_RXLT61EUC4lIZZalUuQKror1NCXkhVyEpNYJDayHdot8O914gB7Uf1gjcdnzpyZsU8w4zmBCO4q_jsrsVBgPU8Y0Es2aSHwoQRTzveUFduYiCPNyHbVpBWVJZEG165pvOlRoEc9vbwElvXlHB0JU8A5g8jZJDAmWLUhJO_8d_SuokWpHps6JQKByTfLsZ37rdRh0sQh4N79B6bBq8cfk-nMcb0E_i-9q9O7On2vdsbZjor6nYg3dHc_lpKwggiJABYCP9-vMs4UzlRUY1oh0EgivrbmMOP1TQ5huTHMHjmdsjU_0Ax1pVlpV5qlWtcZLJ3NEu_2uA-P3Jf41JRgZdcaNN59hTf70mTRpcqmJuIMFrFuwKJr83VaBnmdiWb9TqUijIib9KX7hiVqWbpqX5P0BF1D37GYfWEm8nM1n6yjfuASxNEa4KPmw2lFtt2gwWcQjrXg-XTzAVNRUUa2efrx2g5DE-uugr4JWFHOAGlVnUH8sF5tHrSp9DN-ggNYCJpDpERDBlDXoFH6CE8tWwJVSWqSQKTNHIt9AhN20TEHzH5xXl_DBG-KEqIdrqQ-NYccKzKnWH-U-uYVWhARM94wBVEY-B0JRCf4ByJnbA89N_Qcd-zYoTfyvAF81u5gGPr2p8D33cAdh553GcC_XVp7OB75jg4JbXsUuL4XDCDJqeLiwXzc7v9e_gHAMyyK?type=png)](https://mermaid.live/edit#pako:eNp1U9uO2jAQ_RXLT61EUC4lIZZalUuQKror1NCXkhVyEpNYJDayHdot8O914gB7Uf1gjcdnzpyZsU8w4zmBCO4q_jsrsVBgPU8Y0Es2aSHwoQRTzveUFduYiCPNyHbVpBWVJZEG165pvOlRoEc9vbwElvXlHB0JU8A5g8jZJDAmWLUhJO_8d_SuokWpHps6JQKByTfLsZ37rdRh0sQh4N79B6bBq8cfk-nMcb0E_i-9q9O7On2vdsbZjor6nYg3dHc_lpKwggiJABYCP9-vMs4UzlRUY1oh0EgivrbmMOP1TQ5huTHMHjmdsjU_0Ax1pVlpV5qlWtcZLJ3NEu_2uA-P3Jf41JRgZdcaNN59hTf70mTRpcqmJuIMFrFuwKJr83VaBnmdiWb9TqUijIib9KX7hiVqWbpqX5P0BF1D37GYfWEm8nM1n6yjfuASxNEa4KPmw2lFtt2gwWcQjrXg-XTzAVNRUUa2efrx2g5DE-uugr4JWFHOAGlVnUH8sF5tHrSp9DN-ggNYCJpDpERDBlDXoFH6CE8tWwJVSWqSQKTNHIt9AhN20TEHzH5xXl_DBG-KEqIdrqQ-NYccKzKnWH-U-uYVWhARM94wBVEY-B0JRCf4ByJnbA89N_Qcd-zYoTfyvAF81u5gGPr2p8D33cAdh553GcC_XVp7OB75jg4JbXsUuL4XDCDJqeLiwXzc7v9e_gHAMyyK)
 <br>

 
###  Step 1: User books a flight  
`POST /api/bookings` hits **Booking Service**.

###  Step 2: Booking Service  
✔ Validates flight using Feign  
✔ Saves booking  
✔ Publishes:
- `SeatBookedEvent`
- `BookingConfirmedEvent`

###  Step 3: Flight Service  
✔ Receives `SeatBookedEvent`  
✔ Reduces available seats  
✔ Saves update  

###  Step 4: Email Service  
✔ Receives `BookingConfirmedEvent`  
✔ Sends email using Mailtrap  

---

<img width="1334" height="516" alt="image" src="https://github.com/user-attachments/assets/b46af3db-aeef-4b92-945b-9338cce186ec" />


##  Tech Stack

| Layer | Technology |
|------|------------|
| Discovery | Eureka |
| Gateway | Spring Cloud Gateway WebFlux |
| Communication | Apache Kafka |
| SMTP | Mailtrap |
| Calls | OpenFeign |
| Persistence | MySQL |
| Runtime | Spring Boot |
| Serialization | JSON (Kafka JsonSerializer) |

---

##  Ports

| Service | Port |
|--------|------|
| Eureka Server | 8761 |
| API Gateway | 9090 |
| Flight Service | 8081 |
| Booking Service | 8084 |
| Email Service | 8085 |
| Kafka Broker | 9092 |

---

##  Kafka Topics

| Event | Topic Name |
|-------|------------|
| Seat Booked | `seat-booked-topic` |
| Booking Confirmed | `booking-confirmed` |

---

##  Setup Instructions

### 1️ Start Kafka
```bash
zookeeper-server-start.bat config/zookeeper.properties
kafka-server-start.bat config/server.properties
```

### 2 Start Eureka Server
```bash
mvn spring-boot:run

```
### 3️ Start all Microservices

Run Flight → Booking → Email → API Gateway.


### 4️ Setup Mailtrap SMTP

Use credentials:

Host: sandbox.smtp.mailtrap.io
Port: 587
Username: YOUR_USERNAME
Password: YOUR_PASSWORD


### APIS to call

1. GET http://localhost:9090/api/flight   ---> get all flights
2. GET http://localhost:9090/api/flight/AI-203   ----> get flight by flightNumber
3. POST http://localhost:9090/api/bookings   ---> create a booking
```bash
{
  "flightNumber": "AI-203",
  "contactEmail": "example@mail.com",
  "contactPhone": "9876543210",
  "passengers": [
    {
      "firstName": "Ashu",
      "lastName": "Singh",
      "age": 24,
      "gender": "Male",
      "email": "test@mail.com",
      "phoneNumber": "9876543210"
    }
  ]
}
```


DATABASE SCHEMA  

<img width="655" height="652" alt="image" src="https://github.com/user-attachments/assets/e9330cfa-c6cd-477f-bf5e-1f670ffa4df5" />

###  Features Implemented

1. Microservices Architecture
2. Kafka event-driven flow
3. OpenFeign inter-service calls
4. Email notifications
5. API Gateway routing
6. Eureka discovery
7. Packaged the Application in docker

 


