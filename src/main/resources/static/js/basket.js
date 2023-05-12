// chargement initial du panier
window.addEventListener("load", (event) => {
    updateBasket();
});

// méthode utilitaire de requête
const basketRequest = (method, data) => {
    const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');

    const request = {
        method: method,
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json",
            "X-XSRF-TOKEN": csrfToken,
        },
    }

    if (data)
        request.data = data;

    return fetch('http://localhost:8080/basket', request);
}

// ajouter un produit
const addProduct = (uuid) => {
    basketRequest('POST', { productId: uuid}).then(updateBasket);
}

// enlever un produit
const removeProduct = (uuid) => {
    basketRequest('DELETE', { productId: uuid}).then(updateBasket);
}

// modifier la quantité
const updateProduct = (uuid, quantity) => {
    basketRequest('PUT', { productId: uuid, quantity: quantity}).then(updateBasket);
}

// met à jour l'information du panier courant
const updateBasket = () => {
    basketRequest('GET')
        .then(response => response.json())
        .then(data => {
            const dropdown = document.getElementById('dropdownBasket');
            const elements = document.getElementById('dropdownBasketItems');

            // ajoute le prix total du panier dans la drop down
            dropdown.innerHTML = "";
            dropdown.appendChild(document.createTextNode(`${data.price / 1000} €`));

            // ajoute les éléments du panier
            elements.innerHTML = "";
            data.products.forEach( item => {
                const content = document.createElement('div');
                content.classList.add('dropdown-item');
                content.appendChild(document.createTextNode(`${item.quantity} x ${item.productName}`));

                // remove icon
                const removeIcon = document.createElement('i');
                removeIcon.classList.add('ri-delete-bin-line');

                // remove button
                const removeButton = document.createElement('button');
                removeButton.classList.add('btn');
                removeButton.classList.add('btn-primary');
                removeButton.appendChild(removeIcon);
                content.appendChild(removeButton);
                removeButton.addEventListener("click", (event) => {
                    updateProduct(item.productId, 0);
                });

                const line = document.createElement('li');
                line.appendChild(content);

                elements.appendChild(line);
            });
        });
}