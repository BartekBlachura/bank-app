# APLIKACJA BANKOWA
Prosta aplikacja bankowa napisana w ramach projektu grupowego przez uczestników kursu Akademia Programowania DMT.

### Uczestnicy projeku:
* [Cybińska Aleksandra](https://github.com/dmt-cybinska)
* [Kałamała Agnieszka](https://github.com/dmt-kalamala)
* [Staudt Lucyna](https://github.com/dmt-staudt)
* [Blachura Bartłomiej](https://github.com/BartekBlachura)
* [Gawlas Mateusz](https://github.com/dmt-gawlas)
* [Nowak Jakub](https://github.com/dmt-nowak)
* [Staszkiewicz Grzegorz](https://github.com/dmt-staszkiewiczg)
* [Staszkiewicz Jakub](https://github.com/dmt-staszkiewiczj)

Projekt został zrealizowany w terminie 16.05.2022 - 10.06.2022.

Ogólny schemat budowy i działania aplikacji dostepny jest w postaci mapy myśli pod poniższym [linkiem](https://whimsical.com/bankapp-XfVMWwxEuSbPr2joMen6gZ@2Ux7TurymNDT7tyjifsJ).

---

### Zalożenia projektu:
Celem projektu, jest stworzenie prostej aplikacji bankowej, demonstrując przy tym dobre zasady tworzenia oprogramowania przedstawione w trakcie trwania kursu.

#### Wymagania
* Aplikacja powinna dostarczac nastepujace funkcjonalnosci:
* Mozliwosc rejestracji klienta wraz z tworzeniem unikalnego numeru rachunku
* Mozliwosc "logowania" do swojego konta przy uzyciu nazwy uzytkownika i hasla
* Mozliwosc odroznienia roli administratora systemu od klienta
* Mozliwosc wykonywania przelewow miedzy rachunkami
* Mozliwosc korzystania z lokat i kredytow o oprocentowaniu okreslanym przez administratora systemu
* Mozliwosc odtworzenia historii wlasnych operacji przez kazdego z klientow
* Mozliwosc odtworzenia historii wszystkich operacji przez administratora

#### Szczegoly implementacyjne
* Aplikacja moze dzialac z poziomu linii komend CLI lub korzystac z graficznego interfejsu uzytkownika
* Aplikacja powinna korzystac z udostepnionej bazy danych (pojemnosc max: 1GB) - dostep zostanie przekazany przedstawicielowi grupy
* Kod powinien byc pokryty testami jednostkowymi w przynajmniej 60% (line-coverage)

#### Inne
W ramach zadania, istnieje pelna dowolnosc pod katem projektu bazy danych, ukladu tabel i relacji. Nie ma odgornego ograniczenia pod katem formatu numeru konta, poziomu skomplikowania hasla, czy samego interfejsu.
