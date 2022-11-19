set -e
set -o pipefail
gradle clean
./deployspring.sh
./deploystatic.sh

