# Overview

This repo contains code for the "TensorFlow for poets 2" series of codelabs.

There are multiple versions of this codelab depending on which version 
of the tensorflow libraries you plan on using:

* For [TensorFlow Lite](https://www.tensorflow.org/mobile/tflite/) the new, ground up rewrite targeted at mobile devices
  use [this version of the codelab](https://codelabs.developers.google.com/codelabs/tensorflow-for-poets-2-tflite) 
* For the more mature [TensorFlow Mobile](https://www.tensorflow.org/mobile/mobile_intro) use 
  [this version of the codealab](https://codelabs.developers.google.com/codelabs/tensorflow-for-poets-2).


This repo contains simplified and trimmed down version of tensorflow's example image classification apps.

* The TensorFlow Lite version, in `android/tflite`, comes from [tensorflow/contrib/lite/](https://github.com/tensorflow/tensorflow/tree/master/tensorflow/contrib/lite).
* The Tensorflow Mobile version, in `android/tfmobile`, comes from [tensorflow/examples/android/](https://github.com/tensorflow/tensorflow/tree/master/tensorflow/examples/android).

The `scripts` directory contains helpers for the codelab. Some of these come from the main TensorFlow repository, and are included here so you can use them without also downloading the main TensorFlow repo (they are not part of the TensorFlow `pip` installation).


1. Architektura systemu.

Aplikacja klient-serwer weryfikująca użytkownika na podstawie jego twarzy, oraz wiedzy
ukrytej jaką jest sposób trzymania telefonu podczas robienia zdjęcia.

Aplikacja serwerowa:

https://github.com/MaciejDurski/FaceRecognitionServ

Serwer został stworzony w języku Java. Do komunikacji klient-serwer została użyta
biblioteka Java Sockets. Na serwerze znajduje się uprzednio wytrenowany
model(dostarczony przez TensorFlow) który został poddany procesowi „transfer learning”,
który polega na wytrenowaniu tylko ostatniej warstwy sieci neuronowej. Do procesu
„transfer learning” wykorzystano bibliotekę TensorFlow(https://www.tensorflow.org/)
oraz gotowe skrypty w języku Python dostarczone wraz z biblioteką. Skrypty te
poddano modyfikacjom w celu dostosowania ich do projektu. Zbiór uczący,
który został użyty do treningu jest podzielony na trzy kategorie:
1.Filip – 680 zdjęć
2.Maciej – 968 zdjęć
3.Pozostałe – 53 zdjęć
Na serwerze znajduje się skrypt „autocrop” (https://pypi.org/project/autocrop/) który po
otrzymaniu zdjęcia od klienta wykrywa twarz i przycina zdjęcie tak aby na zdjęciu
pozostała sama twarz użytkownika. Skrypt ten jest dodatkowym zabezpieczeniem
ponieważ jeżeli nie zlokalizuje twarzy na zdjęciu użytkownik dostaje wiadomość zwrotną,
że zdjęcie jest niepoprawne.

Aplikacja klienta:

https://github.com/MaciejDurski/FaceRecognitionAndroid

Klient został stworzony w javie dla systemu Android. Podczas robienia zdjęcia na
urządzeniu zostaje ono zapisane jako bitmapa, a następnie jako tablica bajtów zostaje
zakodowana przy pomocy schematu kodowania Base64. Dane z akcelerometru są
pobierane w momencie wysłania zdjęcia. Odpowiadają one trzem płaszczyzną w jakich
może być ułożony telefon. Każdej płaszczyźnie odpowiada jedna zmienna o wartościach
w zakresie [-10;10]. W momencie rozpoznania użytkownika z serwera pobierane są na
telefon trzy wartości, odpowiednie dla rozpoznanej osoby. Użytkownik zostanie
zautoryzowany przez aplikację, gdy każda ze zmiennych nie będzie się różnić od tych
pobranych z serwera o dwie jednostki. Wszystkie asynchroniczne zadania są wykonane
przez abstrakcyjne klasy AsyncTask.

2. Sposób działania.

![pasted image 0](https://user-images.githubusercontent.com/29012820/47733446-8754ef00-dc68-11e8-8cf1-fe165f865152.png)

1. Użytkownik robi zdjęcie twarzy, następnie wysyła to zdjęcie na serwer. Urządzenie
zapisuje pozycję w jakiej był trzymany telefon w momencie przesłania zdjęcia.

![screenshot_2018-09-28-15-38-02](https://user-images.githubusercontent.com/29012820/47733447-87ed8580-dc68-11e8-9bf1-2b742899c659.png)

2. System wycina twarz ze zdjęcia i przekierowuje je do algorytmu rozpoznającego twarz
![crop](https://user-images.githubusercontent.com/29012820/47733443-8754ef00-dc68-11e8-9f5d-f2059a7bba6c.png)




2 a. jeżeli twarz nie została rozpoznana system wysyła wiadomość (3.) do użytkownika z
informacją, że nie został rozpoznany. Na urządzeniu wyświetla się komunikat, że
użytkownik nie został rozpoznany. W aplikacji mobilnej uruchamia się ekran w którym
użytkownik musi podać swoje imię oraz hasło które są odsyłane na serwer i tam
weryfikowane. Jeżeli hasło się zgadza zdjęcie zostaje dodane do zbioru uczącego i
rozpoczyna się trening sieci neuronowej.

![notrecognized](https://user-images.githubusercontent.com/29012820/47733445-8754ef00-dc68-11e8-94d4-0fc6063aab57.jpg)

2 b. jeżeli twarz została rozpoznana system wysyła wiadomość (3.) do urządzenia z
informacją, że twarz została rozpoznana oraz wartościami akcelerometru dla
użytkownika którego twarz została rozpoznana.


4. Jeżeli urządzenie otrzymało informację o tym, że twarz została rozpoznana to porównuje
zapisaną informację o pozycji w jakiej był trzymany telefon z tą którą otrzymał z serwera. Jeżeli
te pozycje się zgadzają to informuje o tym użytkownika i wpuszcza go do aplikacji.

![zalogowano](https://user-images.githubusercontent.com/29012820/47733448-87ed8580-dc68-11e8-825c-8f5618d79cfe.jpg)





Projekt został stworzony przez
Filipa Wiatrowskiego oraz Macieja Durskiego
w ramach stażu odbywanego na UEP


