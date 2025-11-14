Allow unauthenticated players, such as **custom bots**, to join the game _without authenticating_ through Microsoft.

Use the `/allowplayer <player> [<duration>]` command to allow a player with that username to join for the specified duration(defaulting to 60 seconds), _even if they aren't logged in_ to a valid Microsoft account. 
**This allows players you want on your server to join but prevents random griefers/etc. from joining.**



_Note: Currently in development but at some point the goal is to support 1.20-latest_

## FAQ:
- **Q:** Will this support forge?
  - **A:** It doesn't right now and I have no plans to add support at the moment.
- **Q:** Can you backport it to older versions than 1.20?
  - **A:** Probably not, but you can try using a `fabric_loader_dependency.json` file to override it (be warned that this probably won't work), or submit a PR.
- **Q:** Are these questions actually asked frequently?
  - **A:** I don't know, but they seem like good things to answer
- **Q:** Are you just trying to fill up space on the mod page now?
  - **A:** Yeah because it looks better this way

### Requires [Fabric API](https://modrinth.com/mod/fabric-api/)
