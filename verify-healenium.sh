#!/bin/bash

###############################################################################
# Healenium Service Verification Script
# This script checks if all Healenium services are running properly
###############################################################################

echo "=================================="
echo "Healenium Service Verification"
echo "=================================="
echo ""

# Check if Docker is running
if ! docker info &> /dev/null; then
    echo "❌ Docker is not running!"
    echo "Please start Docker Desktop and try again."
    exit 1
fi

echo "✅ Docker is running"
echo ""

# Check if services are started
echo "📊 Checking service status..."
echo ""

if ! docker-compose ps | grep -q "healenium"; then
    echo "❌ Healenium services are not running"
    echo "Run: ./start-healenium.sh"
    exit 1
fi

# Show service status
docker-compose ps
echo ""

# Check individual services
echo "🔍 Checking individual services..."
echo ""

# PostgreSQL
if docker-compose ps | grep "postgres-db" | grep -q "Up"; then
    echo "✅ PostgreSQL Database: Running on port 5432"
else
    echo "❌ PostgreSQL Database: Not running"
fi

# Healenium Backend
if docker-compose ps | grep "healenium-backend" | grep -q "Up"; then
    echo "✅ Healenium Backend: Running on port 7878"
    
    # Test backend API
    if curl -s http://localhost:7878/actuator/health > /dev/null 2>&1; then
        echo "   ✅ Backend API is responding"
    else
        echo "   ⏳ Backend API not yet ready (may still be starting)"
    fi
else
    echo "❌ Healenium Backend: Not running"
fi

# Selector Imitator
if docker-compose ps | grep "healenium-selector-imitator" | grep -q "Up"; then
    echo "✅ Selector Imitator: Running on port 8000"
else
    echo "❌ Selector Imitator: Not running"
fi

echo ""
echo "=================================="
echo "📍 Access Points"
echo "=================================="
echo ""
echo "🌐 Web UI:      http://localhost:7878/healenium/report"
echo "🔌 Backend API: http://localhost:7878"
echo "🗄️  Database:   localhost:5432 (healenium/healenium_user)"
echo ""

# Check data folders
echo "📁 Data Folders"
echo "=================================="
if [ -d "./db-data" ]; then
    echo "✅ Database data: ./db-data/"
else
    echo "⏳ Database data folder will be created on first run"
fi

if [ -d "./screenshots" ]; then
    echo "✅ Screenshots: ./screenshots/"
else
    echo "⏳ Screenshots folder will be created when healing occurs"
fi

echo ""
echo "💡 Next Steps:"
echo "   1. Enable Healenium in serenity.conf"
echo "   2. Run: mvn clean verify"
echo "   3. View report: http://localhost:7878/healenium/report"
echo ""

