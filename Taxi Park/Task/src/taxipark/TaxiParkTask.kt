package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    this.allDrivers
        .filter { !this.trips.map { it.driver }.toSet().contains(it) }
        .toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
    this.allPassengers
        .filter { p ->
            this.trips
                .map { it.passengers }
                .filter { it.contains(p) }
                .toList().size >= minTrips
        }
        .toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    this.allPassengers
        .filter { p ->
            this.trips
                .filter { it.driver == driver }
                .filter { it.passengers.contains(p) }
                .toList().size > 1
        }
        .toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    this.allPassengers
        .filter { p ->
            val withDiscount = trips.count { t -> p in t.passengers && t.discount != null}
            val withoutDiscount = trips.count { t -> p in t.passengers && t.discount == null}
            withDiscount > withoutDiscount
        }
        .toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    if (this.trips.isNotEmpty()) {
        val result = emptyMap<Int, IntRange>().toMutableMap()
        for (i in 0..trips.map { it.duration }.max()!! step 10)
            result[this.trips.filter { it.duration in IntRange(i, i + 9) }.count()] =
                IntRange(i, i + 9)

        return result.mapKeys { it.key }.maxBy { it.key }?.value
    }
    return null
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean =
    if (this.trips.isNotEmpty())
        this.trips
            .map { it.driver to it.cost }
            .groupBy { it.first }
            .mapValues { it.value.sumByDouble { pair -> pair.second } }
            .values
            .sortedDescending()
            .take((this.allDrivers.size * 0.2).toInt())
            .sum() >= this.trips.map { it.cost }.sum() * 0.8
    else false