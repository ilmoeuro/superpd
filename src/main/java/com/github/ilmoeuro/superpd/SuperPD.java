package com.github.ilmoeuro.superpd;

import java.util.Arrays;

import jvst.wrapper.*;
import jvst.wrapper.valueobjects.*;

public class SuperPD extends VSTPluginAdapter {
  private static final int NUM_PROGRAMS = 16;
  private static final int NUM_OUTPUTS = 1;
  private static final int NUM_VOICES = 16;

  private SuperPDProgram[] programs = new SuperPDProgram[NUM_PROGRAMS];
  private int channelPrograms[] = new int[NUM_PROGRAMS];
  private int currentProgram;
  private SuperPDVoice[] voices = new SuperPDVoice[NUM_VOICES];

  private float srate;
  private float volume;
  private float waveformX;
  private float waveformY;
  private float shift;

  @SuppressWarnings("null")
  public SuperPD(long wrapper) {
    super(wrapper);

    for (int i = 0; i < this.programs.length; i++)
      this.programs[i] = new SuperPDProgram();
    for (int i = 0; i < this.channelPrograms.length; i++)
      this.channelPrograms[i] = i;

    this.setProgram(0);

    this.setNumInputs(0);
    this.setNumOutputs(NUM_OUTPUTS);
    this.canProcessReplacing(true);
    this.isSynth(true);
    this.setUniqueID('S' << 24 | 'u' << 16 | 'P' << 8 | 'D');

    this.srate = 44100f;
    this.waveformX = 0f;
    this.waveformY = 0f;
    this.volume = 1f;
    this.shift = 0f;
    
    for (int i=0; i<NUM_VOICES; i++) {
      voices[i] = new SuperPDVoice();
    }

    this.suspend();
  }

  public void setSampleRate(float s) {
    this.srate = s;
  }

  @SuppressWarnings("deprecation")
  public void resume() {
    this.wantEvents(1); // deprecated as of vst2.4
  }

  public void setProgram(int index) {
    if (index < 0 || index >= NUM_PROGRAMS)
      return;

    SuperPDProgram dp = this.programs[index];
    this.currentProgram = index;

    this.setParameter(SuperPDProgram.PARAM_ID_VOLUME, dp.getVolume());
    this.setParameter(SuperPDProgram.PARAM_ID_WAVEFORM_X, dp.getWaveformX());
    this.setParameter(SuperPDProgram.PARAM_ID_WAVEFORM_Y, dp.getWaveformY());
    this.setParameter(SuperPDProgram.PARAM_ID_SHIFT, dp.getShift());
  }

  public void setProgramName(String name) {
    this.programs[this.currentProgram].setName(name);
  }

  public String getProgramName() {
    String name;

    if (this.programs[this.currentProgram].getName().equals("Init")) {
      name = this.programs[this.currentProgram].getName() + " " + (this.currentProgram + 1);
    } else {
      name = this.programs[this.currentProgram].getName();
    }

    return name;
  }

  public String getParameterLabel(int index) {
    String label = "";

    switch (index) {
    case SuperPDProgram.PARAM_ID_VOLUME:
      label = "dB";
      break;
    default:
      break;
    }

    return label;
  }

  public String getParameterDisplay(int index) {
    String text = "";

    switch (index) {
    case SuperPDProgram.PARAM_ID_VOLUME: 
      text = this.dbToString(this.volume);
      break;
    case SuperPDProgram.PARAM_ID_WAVEFORM_X: 
      text = Float.toString(this.waveformX);
      break;
    case SuperPDProgram.PARAM_ID_WAVEFORM_Y: 
      text = Float.toString(this.waveformY);
      break;
    case SuperPDProgram.PARAM_ID_SHIFT: 
      text = Float.toString(this.shift);
      break;
    default:
      break;
    }

    return text;
  }

  public String getParameterName(int index) {
    String label = "";

    switch (index) {
    case SuperPDProgram.PARAM_ID_VOLUME:
      label = "Volume";
      break;
    case SuperPDProgram.PARAM_ID_WAVEFORM_X:
      label = "Waveform X";
      break;
    case SuperPDProgram.PARAM_ID_WAVEFORM_Y:
      label = "Waveform Y";
      break;
    case SuperPDProgram.PARAM_ID_SHIFT:
      label = "Shift";
      break;
    default:
      break;
    }

    return label;
  }

  public void setParameter(int index, float value) {
    SuperPDProgram dp = this.programs[this.currentProgram];

    switch (index) {
    case SuperPDProgram.PARAM_ID_VOLUME:
      dp.setVolume(value);
      this.volume = value;
      break;
    case SuperPDProgram.PARAM_ID_WAVEFORM_X:
      dp.setWaveformX(value);
      this.waveformX = value;
      break;
    case SuperPDProgram.PARAM_ID_WAVEFORM_Y:
      dp.setWaveformY(value);
      this.waveformY = value;
      break;
    case SuperPDProgram.PARAM_ID_SHIFT:
      dp.setShift(value);
      this.shift = value;
      break;
    default:
      break;
    }

  }

  public float getParameter(int index) {
    float v = 0;

    switch (index) {
    case SuperPDProgram.PARAM_ID_VOLUME:
      v = this.volume;
      break;
    case SuperPDProgram.PARAM_ID_WAVEFORM_X:
      v = this.waveformX;
      break;
    case SuperPDProgram.PARAM_ID_WAVEFORM_Y:
      v = this.waveformY;
      break;
    case SuperPDProgram.PARAM_ID_SHIFT:
      v = this.shift;
      break;
    default:
      break;
    }
    return v;
  }

  @SuppressWarnings("null")
  public VSTPinProperties getOutputProperties(int index) {
    VSTPinProperties ret = null;

    if (index < NUM_OUTPUTS) {
      ret = new VSTPinProperties();
      ret.setLabel("SuperPD " + (index + 1) + "d");
      ret.setFlags(VSTPinProperties.VST_PIN_IS_ACTIVE);
    }

    return ret;
  }

  public String getProgramNameIndexed(int category, int index) {
    String text = "";
    if (index < this.programs.length)
      text = this.programs[index].getName();
    if ("Init".equals(text))
      text = text + " " + index;
    return text;
  }

  public boolean copyProgram(int destination) {
    if (destination < NUM_PROGRAMS) {
      this.programs[destination] = this.programs[this.currentProgram];
      return true;
    }
    return false;
  }

  public String getEffectName() {
    return "SuperPD 0.0.1";
  }

  public String getVendorString() {
    return "Ilmo Euro";
  }

  public String getProductString() {
    return "SuperPD";
  }

  public int getNumPrograms() {
    return NUM_PROGRAMS;
  }

  public int getNumParams() {
    return SuperPDProgram.NUM_PARAMS;
  }

  public boolean setBypass(boolean value) {
    return false;
  }

  public int getProgram() {
    return this.currentProgram;
  }

  public int getPlugCategory() {
    return VSTPluginAdapter.PLUG_CATEG_SYNTH;
  }

  public int canDo(String feature) {
    if (CANDO_PLUG_RECEIVE_VST_EVENTS.equals(feature))
      return CANDO_YES;
    if (CANDO_PLUG_RECEIVE_VST_MIDI_EVENT.equals(feature))
      return CANDO_YES;
    if (CANDO_PLUG_MIDI_PROGRAM_NAMES.equals(feature))
      return CANDO_YES;

    return CANDO_YES;
  }

  public boolean string2Parameter(int index, String value) {
    boolean ret = false;

    try {
      if (value != null)
        this.setParameter(index, Float.parseFloat(value));
      ret = true;
    } catch (Exception e) {
      log(e.toString());
    }

    return ret;
  }

  public boolean getMidiKeyName(long channel, MidiKeyName key) {
    return false;
  }

  private void abstractProcess(float[][] input, float[][] output, int samples, float replaceFactor) {
    float[] out1 = output[0];

    for (int j = 0; j < samples; j++) {
      float sample = 0;
      for (SuperPDVoice voice : Arrays.asList(voices)) {
        if (!voice.isOn()) {
          continue;
        }
        voice.setVcoVal(voice.getVcoVal() + voice.getVcoInc());
        while (voice.getVcoVal() > 1) {
          voice.setVcoVal(voice.getVcoVal() - 1);
        }
        float phase = voice.getVcoVal();
        if (phase < waveformX) {
          float k = waveformX == 0f ? 0 : waveformY/waveformX;
          phase = k*phase;
        } else {
          float q = (waveformX-waveformY)/(waveformX-1f);
          phase = q*(1-phase)+phase;
        }
        sample += (float) Math.sin(phase * 2 * Math.PI + shift * Math.PI)*volume;
      }
      out1[j] = sample + (out1[j] * (1.0f-replaceFactor));
    }
  }

  // DEPRECATED since 2.4
  public void process(float[][] input, float[][] output, int samples) {
    abstractProcess(input, output, samples, 0.0f);
  }

  public void processReplacing(float[][] input, float[][] output, int samples) {
    abstractProcess(input, output, samples, 1.0f);
  }

  public int processEvents(VSTEvents ev) {
    for (int i = 0; i < ev.getNumEvents(); i++) {
      if (ev.getEvents()[i].getType() != VSTEvent.VST_EVENT_MIDI_TYPE)
        continue;

      VSTMidiEvent event = (VSTMidiEvent) ev.getEvents()[i];
      byte[] midiData = event.getData();
      int status = midiData[0] & 0xf0;// ignoring channel

      if (status == 0x90 || status == 0x80) {
        // we only look at notes
        int note = midiData[1] & 0x7f;
        int velocity = midiData[2] & 0x7f;
        if (status == 0x80)
          velocity = 0; // note off by velocity 0

        if (velocity == 0) {
          this.noteOff(note);
        } else {
          this.noteOn(note, velocity >= 64);
        }
      } else if (status == 0xb0) {
        // all notes off
        if (midiData[1] == 0x7e || midiData[1] == 0x7b)
          this.noteOffAll();
      }
    }

    return 1; // want more
  }

  private void noteOn(int note, boolean acc) {
    for (SuperPDVoice voice : Arrays.asList(voices)) {
      if (!voice.isOn()) {
        voice.setNote(note);
        voice.setVcoInc((220f / this.srate) * (float) Math.pow(2f, ((float) note - 57f) * (1f / 12f)));
        voice.setVcoVal(0f);
        voice.setOn(true);
        break;
      }
    }
  }

  private void noteOff(int note) {
    for (SuperPDVoice voice : Arrays.asList(voices)) {
      if (voice.getNote() == note) {
        voice.setOn(false);
      }
    }
  }

  private void noteOffAll() {
    for (SuperPDVoice voice : Arrays.asList(voices)) {
      voice.setOn(false);
    }
  }
}

class SuperPDVoice {
  private float vcoInc = 0;
  private float vcoVal = 0;
  private boolean on;
  private int note;

  public boolean isOn() {
    return on;
  }
  public void setOn(boolean on) {
    this.on = on;
  }
  public int getNote() {
    return note;
  }
  public void setNote(int note) {
    this.note = note;
  }
  public float getVcoInc() {
    return vcoInc;
  }
  public void setVcoInc(float vcoInc) {
    this.vcoInc = vcoInc;
  }
  public float getVcoVal() {
    return vcoVal;
  }
  public void setVcoVal(float vcoVal) {
    this.vcoVal = vcoVal;
  }
}

class SuperPDProgram {
  public final static int PARAM_ID_VOLUME = 0;
  public final static int PARAM_ID_WAVEFORM_X = 1;
  public final static int PARAM_ID_WAVEFORM_Y = 2;
  public final static int PARAM_ID_SHIFT = 3;

  public final static int NUM_PARAMS = PARAM_ID_SHIFT + 1;

  private String name = "Init";

  private float volume = 1f;
  private float waveformX = 0f;
  private float waveformY = 0f;
  private float shift = 0f;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getVolume() {
    return this.volume;
  }

  public void setVolume(float v) {
    this.volume = v;
  }

  public float getWaveformX() {
    return waveformX;
  }

  public void setWaveformX(float waveformX) {
    this.waveformX = waveformX;
  }

  public float getWaveformY() {
    return waveformY;
  }

  public void setWaveformY(float waveformY) {
    this.waveformY = waveformY;
  }

  public float getShift() {
    return shift;
  }

  public void setShift(float shift) {
    this.shift = shift;
  }
}