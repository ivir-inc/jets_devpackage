import sseclient
import json
import base64

def with_urllib3(url, headers):
    """Get a streaming response for the given event feed using urllib3."""
    import urllib3
    http = urllib3.PoolManager()
    return http.request('GET', url, preload_content=False, headers=headers)

def with_requests(url, headers):
    """Get a streaming response for the given event feed using requests."""
    import requests
    return requests.get(url, stream=True, headers=headers)

queryStr = """
subscription medicationSub{
  monitorMedicationTreatment{
    itemIdentifier{
      itemId
      instanceName
      isOwner
    }
    medication
    administrationRoute
    dosageValue
    dosageActive
    dosageTimePeriod
    patientId
    injuryId
    treatmentId
    treatmentTime
    treatmentLocation
  }
}
"""

queryObj = {}
queryObj['query']=queryStr
queryObj['variables']={}
queryObj['operationName']="test"

jsonStr = json.dumps(queryObj)

jsonStrBytes = jsonStr.encode("ascii")
base64_bytes = base64.b64encode(jsonStrBytes)
base64_string = base64_bytes.decode("ascii")


url = 'http://localhost:8080/subscriptions?query=' + base64_string
headers = {'Accept': 'text/event-stream'}
response = with_urllib3(url, headers)  # or with_requests(url, headers)
client = sseclient.SSEClient(response)

for event in client.events():
    print(json.loads(event.data))