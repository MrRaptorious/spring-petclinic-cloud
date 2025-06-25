#!/bin/bash

# The API endpoint URL your microservice provides.
# ADJUST THIS URL TO YOUR ACTUAL ENDPOINT!
# Example: http://localhost/api/invoice/invoice?visitId=8
API_URL="http://localhost/api/invoice/invoice?visitId=8" # <--- Confirm this URL is correct!

echo "Sending 10 requests and showing the responding pod from JSON..."
echo "----------------------------------------"

for i in {1..1000}; do

  response_json=$(curl -s --no-keepalive "$API_URL")
  pod_name=$(echo "$response_json" | jq -r '.servedByPod')

  if [ -z "$pod_name" ] || [ "$pod_name" == "null" ]; then
    echo "  'servedByPod' field not found or empty in JSON response."
    echo "  Full JSON response for diagnosis: $response_json"
  else
    echo "  Served by Pod: $pod_name"
  fi
done

echo "----------------------------------------"
echo "Test completed."