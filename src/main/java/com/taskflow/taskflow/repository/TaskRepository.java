package com.taskflow.taskflow.repository;

import com.taskflow.taskflow.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

// TaskRepository es una interfaz que hereda las operaciones definidas por la interface JpaRepository, 
// especializadas para trabajar con Task y sus identificadores de tipo Long
public interface TaskRepository extends JpaRepository<Task, Long> {
    // ofrece operaciones princiaples para interactuar con bd, como save, findAll, findById, deleteById, ...
    
    // como funciona?
    // Spring, cuando se lanza a ejecutar, busca los repository, y detecta si extienden de 
    // JpaRepository, u otros repositories, 
    // Si lo hace, genera un proxy que implementa la interfaz y lo convierte en bean
        // Proxy -> Es un wrapper de una función, por ejemplo
        // proxyAdd, que es un wrapper de Add(), donde add() puede ser la funcionalidad
        // core, y en el proxy añadimos validaciones antes y despues de llamarla, por ejm:
        // proxyAdd():
        // verificar que sean enteros, etc
        // llama funcion add
        // verifica resultado
        // retorna
        // TECNICAMENTE -> técnicamente un proxy es un objeto intermediario que implementa el mismo contrato que el objeto al que representa

        // Bean -> Es un objeto, el cual su ciclo de vida está manejado por Spring

    // Cuando un Service necesita el Repository, Spring inyecta ese Bean que creo (el que "era antes"
    // el proxy), entonces ese Repository, se le pasa en realidad un proxy, y en Service se trabaja con él

    // Ahora, cómo se genera código/lógica de implementación?
    // El encargado es Spring Data JPA, el cual es una herramienta que, usando una implementación de
    // JPA, en nuestro caso Hibernate, nos permite simplificar interacciones con bd
    // pues nos obvia el como se implementa llamadas y consultas, y las genera él
    // Estas consultas son generadas basandose en el nombre de la funcion en la interface
    // si son las de JpaRepository, sus implementaciones las genera a partir de SimpleJpaRepository, que
    // implementa operaciones CRUD habituales
    // si son declaradas en interface, y no son de SimpleJpaRepository, las genera a partir del nombre de 
    // la función, por lo que este tiene una estructura especifica.
}
