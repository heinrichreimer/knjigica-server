# Vorgehensweise

1. Crawler
-> basiert auf Bash Skripten
- Verwendung der Startseite (Seite, die auf Liberetto verweist) und Erfassung der dort hinterlegten Inhalte
-> Normalisierung der erfassten Inhalte
- Erfassung aller Links (href) und Erweiterung durch Kürzel http
- über "wget" werden zusammengefasste Links heruntergeladen

2. Parser
-> gecrawlte Seiten müssen in Java Objekt geschrieben werden
- Entwurf mehrerer Klassen, die Informationen der Liberetto auf Kerninformationen eingrenzen
- 1. Parser, der HTML Dokumente in das Objekt schreibt
-> Verwendung jsoup
- 2. Parser, der das Objekt in übersichtliches HTML Dokument umwandelt, welches von Suchmaschine als Referenz verwendet wird

3. Einführung Suchmaschine
-> Verwendung von Elasticsearch
- Anpassung der Umgebung und erste Versuche erstellte Dokumente zu durchsuchen