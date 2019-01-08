# Knjigica* - opera libretti search engine

(Fußnote*:
Warum der Titel?
Libretto=mini book
knjigica umgangssprachlich 'mini book')

## Abstract

Sehr kompakte Zusammenfassung, ähnlich wie Buchrücken

## Einleitung

1.Librettos besser verstehen zu können mit Hilfe des Übersetzungen auf fremd Sprachen.
2.Es ist hilfreich für Menschen, die taub sind.Die können die Opern anschauen und den Text dabei lesen.
3.Manchmal ist es schwer den Text zu verstehen, den sie sagen, so dass wir ihn immer finden und die Teile lesen können, die wir nicht verstanden haben.
4.Wenn wir ein Lied mögen, können wir uns nicht sofort daran erinnern und auch nicht den Text sofort merken,so wir haben die Möglichkeit, den gesamten Text anhand der Teile zu finden,
5.Forschung der Librettologie zu unterstützen

## Data

Libretti + Übersetzungen auf verschiedene Sprachen

### Corpora / Datenquellen

Im ersten Schritt der Umsetzung ist eine umfassende Recherche
nach Libretto und deren Texten im Internet erfolgt.

#### Kaerol (Link als Fußnote)

- 385 libretti
- 687 Lieder
- HTML-Format

#### Opera Lib (Link als Fußnote)

- 434 libretti
- HTML-Format
- Stücke mit Prämieren in den Jahren: 1600-1929
- Beispiele für Autoren/Komponisten

<!--
#### Opera Folio (Link als Fußnote)

- noch nicht benutzt
- 512 libretti
- Italienische Libretti vom 18. bis frühes 20. Jahrhundert
- Französische Libretti vom 17. bis 20. Jahrhundert
- Englische Libretti vom 18. Jahrhundert
- Deutsche Libretti vom 18. bis 19. Jahrhundert

#### Opera Glass @ Standford (Link als Fußnote)

- noch nicht benutzt
- 170 Libretti
- 389 Übersetzungen
- 65 Komponisten

#### Aria (Link als Fußnote)

- noch nicht benutzt
- 177 Libretti
- Maintainer/Creator: Rob Glaubitz

#### Teatro La Fenice di Venezia Libretti (Link als Fußnote)

- noch nicht benutzt
- 1792

#### Collection Ulric Voyer (Link als Fußnote)

- noch nicht benutzt
- 220 Libretti
-->

### Datenstruktur

#### Struktur von Libretti

Quelle finden über den strukturellen Aufbau von Libretti. 

Elemente eines Libretto: 


#### Hilfsdatenstruktur 

Wozu haben wir dieses Standardformat?
- zum effizienten Speichern
- zur Vorverarbeitung/Standartisierung

Aus dem allgemeinen Aufbau eines Libretto haben wir für die Suchmaschine ein standartisiertes Format abgeleitet.

Entwurf mehrerer Klassen, die Informationen der Liberetto auf Kerninformationen eingrenzen

Welche informationen werden gespeichert, welche gehen verloren?

## Technische Implementierung

Mit welchen Tools haben wir einen Stack aufgebaut?

Die Implementierung der Suchmaschine erfolgt in mehreren Teilen.

Crawlen, Parsen/Formatieren, Indizieren, Suchmaschine/-Engine, Benutzeroberfläche

### Crawlen

Um an die Inhalte der Corpora zu gelangen 
und somit eine Grundlage für die spätere Suche zu schaffen, 
wurde ein Crawler konzipiert, 
welcher systematisch die HTML-Seiten aller Libretti erfasst 
und diese zunächst unverändert abspeichert. 

- basiert auf Bash Skripten
- HTML-Quelltext der Index-Seite oder der Sitemap des Corpus herunterladen 
- Normalisierung und Parsen des darin hinterlegten HTML-Quellcodes
- alle Links (`href`) extrahieren
- ggf. relative Links mit der absoluten URL erweitern
- über `wget` werden alle referenzierten Seiten heruntergeladen

### Parsen

- Gecrawlte Seiten müssen in Java-Objekt geschrieben werden
- mittels Java

1. Parser, der HTML-Dokumente eines Corpus' in die Hilfsdatenstruktur umwandelt
    - Verwendung Jsoup HTML parser
2. Formatter, der das Objekt in übersichtliches JSON Dokument umwandelt, 
    welches von Suchmaschine (Elasticsearch) als Referenz verwendet wird

### Indizieren
- mittels Java
- Verwendung von Elasticsearch-REST-Client
  - Elasticsearch ist "üblich" in IR
- Anpassung der Umgebung

### Such-Engine

Was ist (sehr grob) Elasticsearch und warum verwenden wir es

### Benutzeroberfläche

Kurz beschreiben, wie man die Benutzeroberfläche gestaltet

## Evaluation

erste Versuche erstellte Dokumente zu durchsuchen

### Herausforderungen

## Zusammenfassung

### Ausblick

## Anhang

### Quellcode

Der Quellcode zu diesem Projekt ist unter https://github.com/heinrichreimer/websearch öffentlich einsehbar.

## Referenzen
einfach unstrukturiert einfügen, sortieren wir später
