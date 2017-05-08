
HoraLunga {
	classvar <synth;
	*initClass {
		StartUp add: {
			Server.default.boot;
		}
	}
	
	*freqs {
		^[17, 24, 29, 36, 29, 38, 24].midicps * [11, 7, 5, 5, 7, 5, 11]
	}
	*tenutas {
		var gui, slider, spec;
		synth = { | amp = 0.1 |
			Mix(SinOsc.ar (this.freqs, 0, amp));
		}.play;

		gui = Window ("TENUTAS").front;
		gui.layout = HLayout (
			slider = Slider ()
		);
		spec = \amp.asSpec;
		slider.action = { | me |
			synth.set (\amp, spec.map (me.value));
		};
		gui.onClose = { synth.free };
	}

	*resonance {
		var gui, decayslider, decayspec, ampslider, ampspec;
		synth = { | decaytime = 1, amp = 0.1 |
			Mix (
				Ringz.ar (
					In.ar (Server.default.options.numOutputBusChannels),
					this.freqs,
					decaytime
				)
			)
		}.play;

		gui = Window ("TENUTAS").front;
		gui.layout = HLayout (
			decayslider = Slider (),
			ampslider = Slider ()
		);
		ampspec = \amp.asSpec;
		ampslider.action = { | me |
			synth.set (\amp, ampspec.map (me.value));
		};
		decayspec = ControlSpec (0.01, 5, \exp, 1);
		decayslider.action = { | me |
			synth.set (\decaytime, decayspec.map (me.value));
		};
		gui.onClose = { synth.free };
	}
}