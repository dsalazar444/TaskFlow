package com.taskflow.taskflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
// JPA genera eventos durante el ciclo de vida de las entidades
// (antes de insertar, actualizar o eliminar, etc.). 
// -> JPA es una especificación (Por ejm: RFC) que define las reglas y herramientas para que podamos trabajar con 
// objetos Java y almacenarlos en una BD sin tener que escribir SQL directamente para cada operación.
// HIBERNATE es la implementación de JPA con la que trabajamos.
// JPA es una especificación de un ORM, que es un Object-Relational Mapping -> técnica que permite mapeo de objeto Java a entidad en BD. 

// Con @EnableJpaAuditing le decimos a Spring:
// "Configura la infraestructura necesaria para habilitar
// la auditoría de entidades".

// La auditoría consiste en mantener información que permita
// conocer la creación y modificación de nuestros registros.

// Con @EntityListeners conectamos nuestras entidades con un listener
// que escucha determinados eventos del ciclo de vida de JPA.
public class TaskflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskflowApplication.class, args);
	}

}
