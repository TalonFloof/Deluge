# Shader Compatibility Guide

NOTE: SHADERS WITH ROUND SUN/MOON SETTINGS MUST HAVE THESE DISABLED FOR DELUGE TO RENDER CORRECTLY, THIS IS A TECHNICAL LIMITATION WITH HOW MINECRAFT SHADERS WORK!

Legend:
- ✅ = Works perfectly, no configuration needed
- ✔️ = Works perfectly, but some configuration needs to be adjusted (see below)
- 🐞 = Works, but with some minor quirks or unfixable issues (see below)
- 🐛 = Major Visual Bugs, don't use
- ❌ = Doesn't work at all

| Shader Name                     | Is Compatible? |
|---------------------------------|----------------|
| VanillAA                        | ✅              |
| Miniature Shader                | ✅              |
| Rudimentary                     | ✅              |
| Tea Shaders                     | ✅              |
| BSL/AstraLex                    | ✔️             |
| Sildur's Vibrant Shaders        | ✔️             |
| MakeUp - Ultra Fast             | ✔️             |
| Pastel Shaders                  | ✔️             |
| Insanity Shader                 | ✔️             |
| FastPBR                         | ✔️             |
| Complementary/Rethinking Voxels | 🐞             |
| Bliss Shader                    | 🐞             |
| Super Duper Vanilla             | 🐞             |
| Nostalgia Shader                | 🐞             |
| Solas Shader                    | 🐛             |
| Photon                          | 🐛             |
| Shrimple                        | 🐛             |
| Lux V1                          | 🐛             |
| Chocapic13' Shaders             | ❌              |
| SEUS Renewed¹                   | ❌              |
| Kappa Shader                    | ❌              |
| Noble Shaders                   | ❌              |
| RenderPearl                     | ❌              |

¹ Other versions of SEUS are non-free. Because of this, they were not tested

> ## BSL/AstraLex
> BSL/AstraLex work great with Deluge! On BSL, Simply change the cloud type to Off and you're good to go! 
> On AstraLex, Disable Round Sun/Moon, Enable RP Custom Sky, and set the Cloud Style to Off.

> ## Sildur's Vibrant Shaders
> Sildur's Vibrant Shaders works really well with Deluge! Enable Default Skybox, set Clouds to Off, and disable Rain Drops.

> ## Complementary/Rethinking Voxels
> Although it is possible to get clouds to render by making the sun/moon style "Reimagined", there is one issue that might affect your experience with Deluge.
> On events with custom fog, the shader rain fog will hide the skybox, making clouds not show up. If you don't mind this, then everything else works just fine, otherwise don't use these shaders.