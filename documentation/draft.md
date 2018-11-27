# Knjigica - opera libretti search engine
(Warum der Titel?)
Libretto=mini book
knjigica umgangssprachlich 'mini book'

## Abstract

**TODO**

## Introduction

**TODO**

### Motivation

1.Librettos besser verstehen zu können mit Hilfe des Übersetzungen auf fremd Sprachen.
2.Es ist hilfreich für Menschen, die taub sind.Die können die Opern anschauen und den Text dabei lesen.
3.Manchmal ist es schwer den Text zu verstehen, den sie sagen, so dass wir ihn immer finden und die Teile lesen können, die wir nicht verstanden haben.
4.Wenn wir ein Lied mögen, können wir uns nicht sofort daran erinnern und auch nicht den Text sofort merken,so wir haben die Möglichkeit, den gesamten Text anhand der Teile zu finden,
5.Forschung der Librettologie zu unterstützen
**TODO**

## Data
Librettos+ Übersetzungen auf verschiedene Sprachen

**TODO**

### Corpus (Corpora)
1.Opera Folio:512 libretti
(Italian:18th till early 20th century
French:17th till 20th century
Englisch:18th century
German:18th till 19th century)

2.Opera Glass @Standford:170 libretti,389 translations,65 composers

3.Aria:177 libretti
(Design:1996-2010
Creator:Rob Glaubitz)

4.Opera Lib:434 libretti
(Years:1600-1929)

5.Teatro La Fenice di Venezia Libretti:
(1792)

6.Collection Ulric Voyer:220 libretti


7.Kaerol:385 libretti,687 songs


**TODO**

### Structure

**TODO**: Find citations about the structure of a libretto. Why did we choose our standard format?
Elements of a Libretto: 
• Character
• Drama 
• Catharsis (climax/major turning point)
• Story
• Message (moral lesson)/ Theme 


## Technical implementation

**TODO**: Java, Elastic Search, ...

### Crawler
- basiert auf Bash Skripten
- Verwendung der Startseite (Seite, die auf Liberetto verweist) und Erfassung der dort hinterlegten Inhalte
- Normalisierung der erfassten Inhalte
- Erfassung aller Links (`href`) und Erweiterung durch Kürzel `http`
- über `wget` werden zusammengefasste Links heruntergeladen

### Parser
- gecrawlte Seiten müssen in Java Objekt geschrieben werden
- Entwurf mehrerer Klassen, die Informationen der Liberetto auf Kerninformationen eingrenzen
1. Parser, der HTML Dokumente in das Objekt schreibt
    - Verwendung Jsoup
2. Formatter, der das Objekt in übersichtliches JSON Dokument umwandelt, welches von Suchmaschine als Referenz verwendet wird

### Indexer
- Verwendung von Elasticsearch
- Anpassung der Umgebung und erste Versuche erstellte Dokumente zu durchsuchen

### Indexing

**TODO**

### Challenges

**TODO**

## Evaluation

**TODO**

## Conclusion

### Outlook

**TODO**

## Appendix

### Source code

**TODO** Link scripts and source repo.

## References

**TODO**
