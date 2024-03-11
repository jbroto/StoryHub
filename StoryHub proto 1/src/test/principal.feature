
Feature: Añadir un elemento a una lista

    Background:
        * url baseUrl

    Scenario: Iniciar sesion
        * path 'login'
        * method get
        * status 200
        * def csrf = karate.extract(response, '"_csrf" value="([ˆ"]*)"', 1)

        * path 'login'
        * form field username = 'a'
        * form field password = 'aa'
        * form field _csrf = csrf
        * method post
        * status 200
    
    Scenario: Crear lista
        # Hemos creado esta url en el controller del usuario
        * path '/user/2/crear-lista'
        * form field listName = 'Favoritos'
        * form field isPublic = 'False'
        * method post
        * status 200
        And match response == 'Favoritos'

    Scenario: Añadir media
        #Esto no hemos sabido como hacerlo

