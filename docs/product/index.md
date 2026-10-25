# Product

This page documents the envisioned product, it's use-cases and features

## Brainstorming

### Use cases

* I as a user can find bike stations with available bikes near me
    * I can see their locations
    * I can see their distances from me
    * I see the bike availability
    * I can specifically ask for ebikes
* Updates? The Bysykkel API exposes real-time status so we could update the user on their selected station while they're
  on their way there 🤔
* The stations are scored by physical walking distance

### Implications for backend

#### First version

* Need the locations of the stations (lat, long)
* Need to supply distance so we don't start hard-coding distance-calculation client-side
* Need to supply how many bikes of each type (bike, ebike) are available
* We'll let the client itself filter if user wants ebike. That way the client can still easily show where regular bikes
  are available even if user

#### Secondary

* Add query parameter for ebike/bike

### Stretch goals

* The stations are scored by the likelyhood that a bike will still be available by the time I get there (even production
  software might not bother to do this lol, but I like this kind of mathematical "correctness")

### API ideation

Context path: `citybike`.

#### Endpoint to fetch the stations sorted by relevance (by long/lat & availability)

```typescript
// GET /citybike/stations?lon={lon}&lat={lat}&max={max}
function fetchStations(lon: number, lat: number, max: number) {
    // lon lat required
    // max optional, but is clamped to a reasonable value, e.g. the range [10, 100]
}
```

#### (Later) Endpoint to fetch historic data?

Could e.g. be cool to show plot of historic trips and say something about how typical a trip was compared to what
terminals other people complete trips between.


