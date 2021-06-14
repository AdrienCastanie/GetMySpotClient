# GetMySpotClient
**Projet :** GetMySpotClient - Application Android
**Autre projet en relation :** GetMySpotServer - API NodeJS - [lien](https://github.com/QuentinPin/GetMySpotServer)
**Description** : Ce projet a été réalisé dans le cadre du cours de développement Android/API à l'ESEO. Il s'agit d'une application permettant aux utilisateurs de publier des photos de lieu qu'ils apprécient au cours de leur balade. Elle permet également de partager d'autres informations en plus de l'image :
* L'adresse du lieu
* L'altitude
* La pression atmosphérique
* La luminosité ambiante
* Le niveau de batterie de l'appareil

Ces lieux sont appellés "spot" dans l'application.


## Informations
Cette application Android est l'interface de l'utilisateur. Sur cette dernière il peut : 
* Se créer un compte et s'y connecter
* Consulter les spots partagés par les autres utilisateurs
* Ajouter un spot
* Consulter son profil (spots personnels que l'on a partagés)

## Demandes ESEO
Dans les conseils pour le projet, il était demandé une collecte des données récupérées des capteurs toutes les X secondes. Afin de correspondre au concept de notre application, ces données sont récupérées uniquement lors de la publication d'un nouveau spot (add spot).
