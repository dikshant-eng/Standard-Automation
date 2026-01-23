#!/bin/bash

###############################################################################
# Healenium Service Manager
# This script helps manage Healenium backend services
###############################################################################

echo "=================================="
echo "Healenium Service Manager"
echo "=================================="
echo ""

# Check if Docker is installed and running
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed!"
    echo "Please install Docker Desktop from: https://www.docker.com/products/docker-desktop"
    exit 1
fi

if ! docker info &> /dev/null; then
    echo "❌ Docker is not running!"
    echo "Please start Docker Desktop and try again."
    exit 1
fi

echo "✅ Docker is running"
echo ""

# Start Healenium services
echo "🚀 Starting Healenium backend services..."
echo ""

docker-compose up -d

echo ""
echo "⏳ Waiting for services to be ready..."
sleep 10

# Check service status
echo ""
echo "📊 Service Status:"
docker-compose ps

echo ""
echo "=================================="
echo "✅ Healenium Services Started!"
echo "=================================="
echo ""
echo "📍 Access Points:"
echo "   - Backend API:  http://localhost:7878"
echo "   - Report UI:    http://localhost:7878/healenium/report"
echo "   - PostgreSQL:   localhost:5432"
echo ""
echo "📋 Useful Commands:"
echo "   View logs:      docker-compose logs -f healenium-backend"
echo "   Stop services:  ./stop-healenium.sh"
echo "   Restart:        docker-compose restart"
echo ""
echo "🧪 Ready to run tests with self-healing! Run:"
echo "   mvn clean verify"
echo ""

