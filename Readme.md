


API Rentals – Spring Boot

API REST permettant la gestion de locations immobilières (CRUD), sécurisée par JWT.
Fonctionnalités : authentification, gestion des utilisateurs, gestion des locations, upload de fichiers, documentation Swagger.


Installation & Lancement du Projet

Prérequis

    Java 17+
    Maven 3.8+
    MySQL 8+
    Un IDE (IntelliJ, VSCode, Eclipse)
    la base de donné avec les information de connection en variable d'environnement.



Lancer l’application

    Dans le dossier du projet :
        mvn spring-boot:run
    L’API démarre sur :
        http://localhost:3001



Swagger UI
    URL swagger = http://localhost:3001/swagger-ui/index.html#/



Github
    URL git = https://github.com/cizuor/ProjJava/tree/release


pour installer la BDD utilisé Mysql, il y a un script dans la parti front, a executer dans mysql, 
les identfiant de connection sont defini comme variable system 
DB_PASSWORD
DB_USER

et le JWT_SECRET qui n'est pas pour la BDD mais doit étre mi aussi 

la Base api_rentals est celle qui est appeler par le programme donc le script doit étre crée dans cette base, 
de plus un utilisateur correspondant au variable d'environnement doit étre crée.