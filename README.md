# Periodicals system
This is a web application with **Spring Boot** backend and **jQuery-Bootstrap-Thymeleaf** frontend.

## Description

Periodicals are magazines, scholarly journals, newspapers, newsletters, etc. They are publications that are published at regular intervals. Daily newspapers, weekly magazines, and quarterly journals are all periodicals.

An **Administrator** manages the catalog of **Publication**s.

A **Reader** can subscribe to **Publication**s from the list.

The system calculates the amount to pay and registers order.

## Minimal requirements

- JDK 1.8

- Apache Maven 3.6.1

- PostreSQL 11.3

## Running app

### Before

- create database with name `periodicals`

- set your username and password for the db in `src/main/resources/application.yml`

- at the first run schema `public` in your db must be empty

- make sure that TCP port for PostgreSQL is `5432`

- you can also change database url in app properties

### Running

- to run app use command `mvn spring-boot:run`

- app will be running at port `8080`

- change port in app properties if needed

## Using app

### Users

By default, there are two users with usernames **u** and **a** with password **p**. 

User **u** is a **Reader** and **a** is an **Administrator**.

### Pages

- `http://localhost:8080` - home page

- `/login` - login page

- `/logout` - logout page

- `/catalog` - view publications and add them to the basket

- `/basket` - view and manage the basket, register order

- `/edit` - manage publications, only accessible by admin

## Tests

- to run tests use command `mvn test -B`

- app use **Travis CI**, so tests will be automatically running after each push to the repo