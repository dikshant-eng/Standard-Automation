#!/bin/bash

###############################################################################
# Stop Healenium Services
###############################################################################

echo "=================================="
echo "Stopping Healenium Services"
echo "=================================="
echo ""

docker-compose down

echo ""
echo "✅ Healenium services stopped successfully!"
echo ""
echo "💡 Data is preserved in db-data/ folder"
echo "   To remove all data: docker-compose down -v"
echo ""

