# A2VFClient

Connecteur Anstel / Vinci Facilities (lien montant).

## Utilisation:
```
java A2VFClient [-ifxserver ifxdb] [-mgoserver mgodb] [-apiserver apiserver] [-d] [-t] 
```
où :
* ```-apiserver apiserver``` est la référence au serveur d'API distant, par défaut désigne le serveur de développement Voir fichier *A2VFClient.prop* (paramètre optionnel).
* ```-ifxserver ifxdb``` est la référence à la base de données Informix, par défaut désigne la base de données de développement. Voir fichier *A2VFClient.prop* (optionnel).
* ```-mgoserver mgodb``` est la référence à la base de données Mongo, par défaut désigne la base de données de développement. Voir fichier *A2VFClient.prop* (optionnel).
* ```-d``` le programme s'exécute en mode débug, il est beaucoup plus verbeux. Désactivé par défaut (paramètre optionnel).
* ```-t``` le programme s'exécute en mode test, les transcations en base de données ne sont pas faites. Désactivé par défaut (paramètre optionnel).

## Pré-requis :
- Java 6 ou supérieur.
- Driver Mongodb pour Java
- JDBC Informix
- Jackson
- OKHttp
- OKIO
- Kotlin
- JavaMail API
- Log4j

## Fichier des paramètres : 

Ce fichier permet de spécifier les paramètres d'accès aux différentes bases de données.

A adapter selon les implémentations locales.

Ce fichier est nommé : *A2VFClient.prop*.

Le fichier *A2VFClient_Example.prop* est fourni à titre d'exemple.

## Références:

- [GitHub OKHttp](https://square.github.io/okhttp/)
- [GitHub OKIO](https://github.com/square/okio)
- [Tuto A Guide to OkHttp](https://www.baeldung.com/guide-to-okhttp)
- [Pour valider un Json](https://jsonformatter.curiousconcept.com/)
- [JavaMail API](https://javaee.github.io/javamail)
- [Log4J](http://logging.apache.org/log4j/2.x/download.html)
- [Tuto Log4J](https://koor.fr/Java/Tutorial/java_logging_log4j2.wp)
