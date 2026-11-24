# Galaxy Watch sensor apps — UNCC research project

**Three separate Wear OS apps**, one per sensor, sharing a small `core` library.
Each installs as its own app on the watch with its own icon.

| Module | App name on watch | Sensor | Data today | Real-watch path |
|--------|-------------------|--------|------------|-----------------|
| `hr`   | **HR Monitor**    | Optical heart rate |  live BPM + HRV (synthetic) | Health Services (works) → Samsung SDK for raw PPG/HRV |
| `ecg`  | **ECG Monitor**   | Electrical (ECG)   | live synthetic waveform |  Samsung **Privileged** SDK (partnership) |
| `bia`  | **Body Composition** | Bioimpedance (BIA) | synthetic on-demand readings |  Samsung **Privileged** SDK (partnership) |
| `core` | _(library)_       | —      | CSV logging, curved button | shared by all three |

> Each app has a `DataSourceConfig.USE_DUMMY` flag. It's `true` now (synthetic data,
> runs on the emulator, no permissions). Flip to `false` to use the real sensor.
> For **HR** that immediately works via Health Services. For **ECG/BIA** the real
> source is a stubbed `Privileged*Source` that needs Samsung's Privileged Health SDK
> (partnership/NDA) wired in first — see those files' comments.

---

## Run an app

1. Open the `GalaxyWatchHeart` folder in Android Studio; let Gradle sync.
2. Start your **Wear OS Large Round** emulator (Tools → Device Manager).
3. In the **run-configuration dropdown** at the top, pick the module to run: **hr**, **ecg**, or **bia**.
4. Click **Run ▶**. The app installs and launches on the watch.

Repeat step 3–4 for each app; they coexist as three separate apps in the watch's app list.

### What each app shows
- **HR Monitor** — big live BPM, HRV (RMSSD), a curved **Start/Stop recording** button.
- **ECG Monitor** — a live green ECG waveform, latest mV, curved recording button.
- **Body Composition** — body fat / muscle / water, a curved **Measure** button (each
  press takes one reading and appends it to the CSV).

---

## Recorded data (CSV)
Each app writes timestamped CSV sessions to its own external files dir. Pull them with:
```
adb pull /sdcard/Android/data/com.uncc.galaxyheart.hr/files/sessions ./hr
adb pull /sdcard/Android/data/com.uncc.galaxyheart.ecg/files/sessions ./ecg
adb pull /sdcard/Android/data/com.uncc.galaxyheart.bia/files/sessions ./bia
```
- HR: `timestamp_ms,bpm,ibi_ms`
- ECG: `timestamp_ms,mv`
- BIA: `timestamp_ms,body_fat_pct,skeletal_muscle_kg,body_water_pct`

---

## Switching to the real watch 
Per app, edit its `data/DataSourceConfig.kt` and set `USE_DUMMY = false`.
- **HR** ([hr/.../data/DataSourceConfig.kt](hr/src/main/java/com/uncc/galaxyheart/hr/data/DataSourceConfig.kt)) — done, reads the optical sensor (asks for body-sensors permission).
- **ECG / BIA** — first implement `PrivilegedEcgSource` / `PrivilegedBiaSource` using the
  Samsung Privileged Health SDK, then flip the flag. Until the partnership is granted
  they correctly report "Requires Samsung Privileged SDK".

## Project layout
```
GalaxyWatchHeart/
├─ settings.gradle.kts          # includes :core, :hr, :ecg, :bia
├─ core/                        # CsvLogger, CurvedBottomButton (shared)
├─ hr/                          # HR Monitor app
├─ ecg/                         # ECG Monitor app
└─ bia/                         # Body Composition app
```


