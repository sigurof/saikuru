# Product

This page documents the envisioned product, it's use-cases and features

## Brainstorming

### Use cases

* I as a user can find bike stations with available bikes near me
    * I can see their locations
    * I see the bike availability
* Updates? The Bysykkel API exposes real-time status so we could update the user on their selected station while they're
  on their way there 🤔
* The stations are scored by physical walking distance
*

### Stretch goals

* The stations are scored by the likelyhood that a bike will still be available by the time I get there (even production
  software might not bother to do this lol, but I like this kind of mathematical "correctness")

### API ideation

Context path: `citybikes`.

#### Endpoint to fetch the stations sorted by relevance (by long/lat & availability)

```typescript
// GET /citybikes/stations?lon={lon}&lat={lat}&max={max}
function fetchStations(lon: number, lat: number, max: number) {
    // lon lat required
    // max optional, but is clamped to a reasonable value, e.g. the range [10, 100]
}
```

#### (Later) Endpoint to fetch historic data?

Could e.g. be cool to show plot of historic trips and say something about how typical a trip was compared to what
terminals other people complete trips between.


