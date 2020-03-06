# https://stackoverflow.com/questions/226703/how-do-i-prompt-for-input-in-a-linux-shell-script
while true; do
    read -p "Do you wish to install this program?" yn
    case $yn in
        [Yy]* ) make install; break;;
        [Nn]* ) exit;;
        * ) echo "Please answer yes or no.";;
    esac
done

echo "Do you wish to install this program?"
select yn in "Yes" "No"; do
    case $yn in
        Yes ) make install; break;;
        No ) exit;;
    esac
done