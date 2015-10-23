# Henri Potier

<a href="https://travis-ci.org/geekarist/hpo">
<img src="https://travis-ci.org/geekarist/hpo.svg?branch=master">
</a>

## Introduction

Le but de ce document est de décrire le nouvel exercice mobile demandé au candidat lors de leur passage en entretien. Ce document contient le même sujet que la fondation front mais adaptée au mobile :

https://docs.google.com/a/xebia.fr/document/d/1NBGv2hp4b3c0wocNuP_X3d0OXWb8UrMnJJkQqdMrRBU

## Énoncé

Il était une fois, une collection de cinq livres racontant les histoires d’un formidable héros nommé Henri Potier. Tous les enfants du monde trouvaient les histoires de cet adolescent fantastiques. L’éditeur de cette collection, dans un immense élan de générosité (mais aussi pour booster ses ventes ;)), décida de mettre en place des offres commerciales aussi aléatoires que l’issue des sorts de Ron Weasley.
L’éditeur vous demande de développer une application mobile permettant d’acheter les livres de l’éditeur. L’application sera composée de deux interfaces, la première permet de choisir les livres que l’on veut acheter. La seconde récapitule le panier, où sera appliquée la meilleure offre commerciale possible parmi les différentes proposées.

La liste des livres Henri Potier est accessible à l’adresse http://henri-potier.xebia.fr/books en GET.

Et les offres commerciales associées sont disponibles (depuis n’importe quel domaine) en GET à l’adresse :

http://henri-potier.xebia.fr/books/{ISBN1,ISBN2,...}/commercialOffers 

Le paramètre est la liste des ISBN distincts des livres du panier.

Par exemple pour deux livres (à 35€ et à 30€), la requête ressemblera à :

http://henri-potier.xebia.fr/books/c8fabf68-8374-48fe-a7ea-a00ccd07afff,a460afed-e5e7-4e39-a39d-c885c05db861/commercialOffers

Et le service vous renverra alors les offres applicables à ce panier sous le format :

    {
        "offers": [
            {"type": "percentage", "value": 5},
            {"type": "minus", "value": 15},
            {"type": "slice", "sliceValue": 100, "value": 12}
        ]
    }

Lien pastebin.com : http://pastebin.com/RDpeqQyX

Le prix attendu pour ce panier devra être alors 50€.

Explications:

La première offre identifiée par un type ‘percentage’ est une réduction s’appliquant sur le prix de l’ensemble des livres. Le montant de la réduction est dans ‘value’ ;

La deuxième offre identifiée par un type ‘minus’ est une déduction directement applicable en caisse d’un montant de ‘value’ ;

La troisième offre identifiée par un type ‘slice’ est un remboursement par tranche d’achat. Dans cet exemple, on rembourse 12€ par tranche de 100€ d’achat.
