module unifood

--Signatures--

some sig University {
	campus: some Campus
}

sig Campus {
	restaurants: some Restaurant
}

sig User {
	type: one Type,
}

abstract sig Type {}

sig Student extends Type {
	favRestaurant: some Restaurant,
	sCampus: one Campus
}

sig Owner extends Type {
	restaurant: one Restaurant
}

sig Restaurant {
	rCampus: one Campus,
	owner: one Owner,
	products: some Product,
	reviews: some Review
}

sig Product {}

sig Review {
	rUser: one Student,
	rRestaurant: one Restaurant
}


--Facts--

fact University_and_Campus{
	all u:University | some u.campus
	all c:Campus | some c.~campus
	all c:Campus, u1:University, u2:University | (c in u1.campus) and (c in u2.campus) => u1 = u2
}

fact Student{

}

fact Restaurant{
	all r:Restaurant, c1:Campus, c2:Campus | (r in c1.restaurants) and (r in c2.restaurants) => c1 = c2
	all p:Product, r1:Restaurant, r2:Restaurant | (p in r1.products) and (p in r2.products) => r1 = r2
	all r:Restaurant, o1:Owner, o2:Owner | (r in o1.restaurant) and (r in o2.restaurant) => o1 = o2
	all r:Restaurant | one c:Campus | r in c.restaurants
}

fact usersInteractInsideCampus{
	all u:User| let  t = u.type{
	t in Student => t.favRestaurant in t.sCampus.restaurants
	}
}

fact GoAndBackRestaurantAndCampus{
	all rest:Restaurant| rest in rest.rCampus.restaurants
}

fact GoAndBackRestaurantReview{
	all r:Restaurant| all rv:r.reviews| rv.rRestaurant = r
}

fact ReviewsInsideCampus{
all s:Student,rv:Review{
 s in rv.rUser=> rv.rRestaurant.rCampus = s.sCampus
	}
}


fact {
	all u:User | one u.type
	all t:Type | some t.~type

	all s:Student | one s.sCampus
	all r:Restaurant | one r.rCampus
	all c:Campus | some c.~sCampus

	all p:Product | some p.~products
	all r:Review | some r.~reviews

	all o:Owner | one o.restaurant
	all r:Restaurant | some r.~restaurant
	all r:Review, r1:Restaurant, r2:Restaurant | (r in r1.reviews) and (r in r2.reviews) => r1 = r2
	all t:Type, u1:User, u2:User | (t in u1.type) and (t in u2.type) => u1 = u2

}


--Predicates--

pred show[]{

}

run show for 4
