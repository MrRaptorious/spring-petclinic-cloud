import http from 'k6/http';
import { check, sleep } from 'k6';
import { SharedArray } from 'k6/data';
import { vu } from 'k6/execution';

// --- Konfiguration des Lasttests ---
export const options = {
  // Eine Minute Lasttest
  duration: '1m', 
  // 20 virtuelle Benutzer (vus), die Anfragen senden
  vus: 20,       
  // Schwellenwerte für erfolgreichen Testlauf
  thresholds: {
    // 95% aller HTTP-Anfragen müssen schneller als 300ms sein
    http_req_duration: ['p(95)<300'], 
    // Die Rate der fehlgeschlagenen Überprüfungen muss unter 1% liegen
    checks: ['rate>0.99'],             
    // Die Rate der HTTP-Fehler (z.B. 5xx Status Codes) muss unter 0.1% liegen
    http_req_failed: ['rate<0.001'],
  },
};

// --- Daten vorbereiten (optional, aber gut für realistische Tests) ---
// Wenn deine Datenbank Rechnungen mit bestimmten visitId's enthält,
// kannst du diese hier verwenden. Für den POST-Request erzeugen wir zufällige Daten.
const visitIds = new SharedArray('visitIds', function () {
  // Dies sind Beispiel-Visit-IDs. Passe sie an die IDs an, die in deiner Datenbank existieren.
  // Idealerweise füllst du deine Datenbank vor dem Test mit Testdaten.
  return Array.from({ length: 4 }, (_, i) => i + 1); // Generiert IDs von 1 bis 100
});


// --- Haupt-Test-Funktion ---
export default function () {
  // Basis-URL deiner Docker-Anwendung (achte auf den richtigen Port!)
  const BASE_URL = 'http://localhost/api/invoice'; 

  // 1. Testfall: GET-Anfrage an /invoice (Abrufen einer Rechnung)
  // Wir wählen eine zufällige visitId aus unserem vorbereiteten Array
  const randomVisitIdForGet = visitIds[Math.floor(Math.random() * visitIds.length)];
  let getRes = http.get(`${BASE_URL}/invoice?visitId=${randomVisitIdForGet}`);

  // Überprüfung der GET-Antwort
  check(getRes, {
    'GET /invoice status is 200': (r) => r.status === 200 || r.status === 404, // 404 ist ok, wenn keine Rechnung gefunden wird
    'GET /invoice response body not empty': (r) => r.body.length > 0,
  });
  
  // 2. Testfall: POST-Anfrage an /visits/{visitId}/invoice (Erstellen einer Rechnung)
  // Wir erzeugen eine neue, zufällige visitId für den POST, um Kollisionen zu vermeiden
  // In einer echten Umgebung sollte die visitId aus einem System kommen oder eindeutig sein
  const newVisitIdForPost = 1;

  const invoicePayload = JSON.stringify({
    // Hier kannst du die Felder deiner Invoice-Klasse entsprechend anpassen
    // Diese Werte sollten zu deinem Invoice-Modell passen
    id: 1,
    visitId: 1, 
    amount: Math.floor(Math.random() * 500) + 10,
    dueDate: new Date().toISOString().split('T')[0],
    status: "PENDING"
  });

  const postParams = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  let postRes = http.post(`${BASE_URL}/visits/${newVisitIdForPost}/invoice`, invoicePayload, postParams);

  // Überprüfung der POST-Antwort
  check(postRes, {
    'POST /visits/{visitId}/invoice status is 201 Created': (r) => r.status === 201,
    'POST /visits/{visitId}/invoice response body not empty': (r) => r.body.length > 0,
    'POST /visits/{visitId}/invoice response contains visitId': (r) => {
        try {
            const json = JSON.parse(r.body);
            return json.visitId === newVisitIdForPost;
        } catch (e) {
            return false;
        }
    },
  });

  // Kurze Pause, um das Verhalten eines echten Benutzers zu simulieren
  sleep(1); 
}