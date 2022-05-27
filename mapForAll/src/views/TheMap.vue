<template>
  <NavBar />
  <div id="map"></div>
  <form>
    <div class="multi-select">
      <label class="toggle-open" for="toggle-open">Select States</label>
      <input type="checkbox" id="toggle-open" />
      <ul class="multi-select-options">
        <li>
          <label>
						<input
              type="checkbox"
              name="states"
              value="AL"
              data-state="AL"
            /><span>Alabama</span>
					</label>
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="AK"
              data-state="AK"
            /><span>Alaska</span>
					</label>
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="AZ"
              data-state="AZ"
            /><span>Arizona</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="AR"
              data-state="AR"
            /><span>Arkansas</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="CA"
              data-state="CA"
            /><span>California</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="CO"
              data-state="CO"
            /><span>Colorado</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="CT"
              data-state="CT"
            /><span>Connecticut</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="DE"
              data-state="DE"
            /><span>Delaware</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="DC"
              data-state="DC"
            /><span>District Of Columbia</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="FL"
              data-state="FL"
            /><span>Florida</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="GA"
              data-state="GA"
            /><span>Georgia</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="HI"
              data-state="HI"
            /><span>Hawaii</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="ID"
              data-state="ID"
            /><span>Idaho</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="IL"
              data-state="IL"
            /><span>Illinois</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="IN"
              data-state="IN"
            /><span>Indiana</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="IA"
              data-state="IA"
            /><span>Iowa</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="KS"
              data-state="KS"
            /><span>Kansas</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="LA"
              data-state="LA"
            /><span>Louisiana</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="ME"
              data-state="ME"
            /><span>Maine</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="MD"
              data-state="MD"
            /><span>Maryland</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="MA"
              data-state="MA"
            /><span>Massachusetts</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="MI"
              data-state="MI"
            /><span>Michigan</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="MN"
              data-state="MN"
            /><span>Minnesota</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="MS"
              data-state="MS"
            /><span>Mississippi</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="MO"
              data-state="MO"
            /><span>Missouri</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="MT"
              data-state="MT"
            /><span>Montana</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="NE"
              data-state="NE"
            /><span>Nebraska</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="NV"
              data-state="NV"
            /><span>Nevada</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="NH"
              data-state="NH"
            /><span>New Hampshire</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="NJ"
              data-state="NJ"
            /><span>New Jersey</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="NM"
              data-state="NM"
            /><span>New Mexico</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="NY"
              data-state="NY"
            /><span>New York</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="NC"
              data-state="NC"
            /><span>North Carolina</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="ND"
              data-state="ND"
            /><span>North Dakota</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="OH"
              data-state="OH"
            /><span>Ohio</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="OK"
              data-state="OK"
            /><span>Oklahoma</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="OR"
              data-state="OR"
            /><span>Oregon</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="PA"
              data-state="PA"
            /><span>Pennsylvania</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="RI"
              data-state="RI"
            /><span>Rhode Island</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="SC"
              data-state="SC"
            /><span>South Carolina</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="SD"
              data-state="SD"
            /><span>South Dakota</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="TN"
              data-state="TN"
            /><span>Tennessee</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="TX"
              data-state="TX"
            /><span>Texas</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="UT"
              data-state="UT"
            /><span>Utah</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="VT"
              data-state="VT"
            /><span>Vermont</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="VA"
              data-state="VA"
            /><span>Virginia</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="WA"
              data-state="WA"
            /><span>Washington</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="WV"
              data-state="WV"
            /><span>West Virginia</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="WI"
              data-state="WI"
            /><span>Wisconsin</span></label
          >
        </li>
        <li>
          <label
            ><input
              type="checkbox"
              name="states"
              value="WY"
              data-state="WY"
            /><span>Wyoming</span></label
          >
        </li>
      </ul>
    </div>
    <ul class="multi-selected"></ul>
  </form>
</template>

<script lang='ts'>
import NavBar from "../components/nav.vue";
export default {
  name: "TheMap",
  components: {
    NavBar,
  },
};
</script>

<script setup lang='ts'>
import mapboxgl from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import { onMounted } from "@vue/runtime-core";
import { ref } from "vue";

mapboxgl.accessToken =
  "pk.eyJ1Ijoiam9obm55dCIsImEiOiJja2xxNXplNjYwNnhzMm5uYTJtdHVlbTByIn0.f1GfZbFLWjiEayI6hb_Qvg";

onMounted(() => {
  const map = new mapboxgl.Map({
    container: "map",
    style: "mapbox://styles/johnnyt/ckp3ijl0l093w17pqe4bl8s9b",
    center: [115.951, 29.517],
    zoom: 12.89,
    pitch: 60,
  });
});
</script>


<style lang='scss' scope>
div#map {
  width: 100vw;
  height: 94vh;
  padding-top: 6vh;
}

form {
	position: absolute;
	right: 3vw;
	top: 5vh;
  margin: 30px auto;
  width: 20vw;

	div.multi-select {
		&, ul.multi-select-options {
			background: rgba(255, 255, 255, 0.5);
			width: 100%;
		}

		display: block;
		position: relative;
		line-height: 35px;
		border: 1px solid #ddd;
		margin: 0 0 50px;
		padding: 0;
		border-radius: 1em;

		label {
			margin: 0;
			padding: 0;

			&.toggle-open {
			display: block;
			font-size: 1em;
			font-weight: bold;
			padding: 0 10px;
			height: 3.6vh;
			width: auto;
			line-height: 3.6vh;
			cursor: pointer;
				&:after {
					clear: both;
					display: block;
					position: absolute;
					top: 1.6vh;
					right: 0.6vw;
					content: "";
					height: 0;
					width: 0;
					padding: 0;
					margin: 0 auto;
					border: 8px solid transparent;
					border-top-color: #07afef;
				}
			}
		}

		input[type=checkbox] {
			display: none;
		}

		ul.multi-select-options {
			position: absolute;
			top: 3.8vh;
			left: 0;
			list-style: none;
			margin: 0;
			padding: 0;
			overflow: auto;
			border-radius: 1em;
			&::-webkit-scrollbar {
				display: none;
			}
			& > li {
				&:first-child {
					span {
						border-top: 1px solid #ddd;
					}
				}
				span {
					display: none;
					height: 3.6vh;
					width: auto;
					padding: 5px 10px;
					background: rgba(255, 255, 255, 0.5);
					border: 1px solid #ddd;
					border-top: 0;
					line-height: 3.6vh;
					cursor: pointer;
					&:after {
						content: "\002B";
						display: inline-block;
						font-size: 1.5em;
						font-weight: bold;
						position: absolute;
						right: 10px;
						z-index: 99;
						border-radius: 1ems;
					}
					&:hover {
						background: rgba(214, 214, 214, 0.5);
					}
				}
				input[type=checkbox]:checked {
					& + span {
						background: rgba(164, 203, 234, 0.5);
						font-weight: bold;
						&:after {
							content: "\2212";
							font-size: 1.3em;
						}
					}
				}
			}
		}

		input#toggle-open {
			&:checked {
				& ~ .multi-select-options {
					height: 200px;
				}
			}
			&:not(:checked) {
				& ~ .multi-select-options {
					display: block;
					background: none;
					border: none;
					padding: 5px 0;
					input[type=checkbox]:checked {
						& + span {
							display: block;
							float: left;
							margin: 0 0.5em 0.5em 0;
							padding: 0px 5px;
							width: auto;
							height: 2.4em;
							position: inherit;
							top: 0;
							border-top: 1px solid #ddd;
							line-height: 2.4em;
							border-radius: 1em;
							
							&:before {
								content: '\2A09';
								padding: 0 5px 0 0;
							}
							&:after {
								content: '';
								border-radius: 1em;
							}
						}
					}
				}
			}
		}
		#toggle-open:checked ~ .multi-select-options label,
		#toggle-open:checked ~ .multi-select-options span {
			display: block;
		}
	}
	
	
}
</style>