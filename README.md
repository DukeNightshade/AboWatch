# Projekt-Steckbrief: „AboWatch“
## 1. Kernkonzept
Eine leichtgewichtige, Privacy-First Android-App zur manuellen Verwaltung von Abonnements. 
Die App dient als zentrale Übersicht, um Fixkosten zu kontrollieren und Kündigungsfristen nie wieder zu verpassen. 
Alle Daten verbleiben lokal auf dem Gerät des Nutzers (kein Cloud-Zwang, kein Bankkonten-Zugriff).

## 2. Technische Rahmenbedingungen

- Entwicklungsumgebung: Android Studio
- Programmiersprache (Backend): Java
- UI-Technologie: XML-Layouts mit Material Design 3 (Material You)
- Datenhaltung: Room Persistence Library (SQLite-Wrapper für Java)

## 3. Funktionale Anforderungen (Features)
   
- Dashboard: Anzeige der monatlichen Gesamtausgaben auf einen Blick.
- Abo-Liste: Übersicht aller aktiven Abos in einer modernen Karten-Ansicht (RecyclerView).

### Detail-Erfassung:

- Name des Dienstes (z. B. Netflix, Fitnessstudio)
- Preis & Intervall (monatlich, jährlich)
- Kategorie (Streaming, Sport, Software, etc.)
- Startdatum des Abos
- Kündigungsfrist (z. B. „14 Tage vor Ablauf“)
- Status-Berechnung: Automatische Anzeige, wie lange ein Abo bereits läuft und wann der nächste Kündigungstermin ist.
- Kategorisierung: Filtern der Liste nach Kategorien.

## 4. Design-Vorgaben (UI/UX)
- Stil: Modernes, flaches Design basierend auf Material Design 3.
### Elemente:

- FloatingActionButton (FAB) zum schnellen Hinzufügen neuer Abos.
- CardViews für die Listeneinträge mit abgerundeten Ecken.
- MaterialToolbar für die Navigation und Titelanzeige.
- Unterstützung für Dark Mode (Standard in Material 3).
