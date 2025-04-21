#!/bin/bash

# === Settings ===
REALM_NAME="SSO"
EXPORT_DIR="/opt/keycloak/data/export"
LOCAL_EXPORT_PATH="./keycloak/realm-export.json"
CONTAINER_NAME="keycloak"

echo "🔄 Syncing realm: $REALM_NAME"

# Step 1: Run export inside the container
echo "📦 Exporting realm inside container..."
docker exec -it $CONTAINER_NAME /opt/keycloak/bin/kc.sh export --dir $EXPORT_DIR --realm $REALM_NAME --optimized

# Step 2: Copy file from container to local machine
echo "📥 Copying export to local..."
docker cp $CONTAINER_NAME:$EXPORT_DIR/$REALM_NAME-realm.json $LOCAL_EXPORT_PATH

# Step 3: Commit to Git
echo "✅ Export successful. Committing to Git..."
git add $LOCAL_EXPORT_PATH
git commit -m "🔄 Sync: Exported $REALM_NAME realm"
git push

echo "🎉 Done! Realm exported and pushed to repository."
