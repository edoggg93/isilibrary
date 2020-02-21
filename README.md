# isilibrary

Librearia che si interfaccia con l'applicazione IsiApp del progetto Isi.

Tutti i diritti riservati.

Copyright: 4D Sistemi Informatici. Contattare l'amministrazione per eventuale supporto tecnico e permessi.

Come utilizzare la libreria:

. La libreria lavora se e solo se sul dispositivo è stato installato il progetto IsiApp
. Nel Gradle dell'applicazione inserire:
	allprojects {  
        repositories {  
            maven { url 'https://jitpack.io' }  
        }  
    }  

. Sempre nel gradle importare:
    implementation 'com.github.edoggg93:isilibrary:1.2.0'  

. L'activity che dovrà integrare il sistema dovrà estendere la IsiAppActivity.  
. In caso di Scrollview: la scrollview dovrà essere IsiAppScrollView  
. In caso di webview: la webView dovrà essere IsiAppWebView  
. L'applicazione IsiApp ha in se un timer che permette l'interruzione di tutte le altre applicazioni presenti nel sistema;  
  per svolgere operazoni prima della chiusura, fare l'override, nell'activity che estende IsiAppActivity, del metodo doSomethingOnTimeout()  
  
  
Grazie.  

