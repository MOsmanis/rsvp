import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Form from 'react-bootstrap/Form'
import Button from 'react-bootstrap/Button'
import { useEffect, useState } from 'react'
import Spinner from 'react-bootstrap/Spinner'
import { useParams } from 'react-router-dom'
import {Guest} from './Guests'
import envelope from './envelope.svg'


export interface Family {
  id: number;
  welcomeMsg: string;
  members: Guest[];
  lastResponseDate: string;
} 

function App() {
  const { inviteCode } = useParams();
  const [family, setFamily] = useState<Family>({id: 1, welcomeMsg: "", members: [], lastResponseDate: ""})

  const [loading, setLoading] = useState(false)

  const getFamily = async () => {
    fetch("http://localhost:8080/family/" + inviteCode)
    .then(res => res.json())
    .then(
        (result) => {          
          result.members.map((r: Guest) => {
            r.hasDiet =  r.dietaryPreference.trim() === "" ? false : true;
            return r;
          })     
          setFamily(result)
        },
        (error) => {
          console.log(error)
          setFamily({id: 1, welcomeMsg: "", members: [], lastResponseDate: ""})
        }
    )
  }

  useEffect(() => {
      getFamily();
  }, [])

  const onSubmitAttendance = (event: any) => {
    event.preventDefault();
    setLoading(true)
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(family)
    };
    fetch('http://localhost:8080/submit', requestOptions)
    .then(response => {
      setLoading(false)
      if(response.ok) {
        getFamily();
        event.target.reset();
      } 
    });
  }

  function handleAttendanceChange(index: number) {
    const guest = family.members[index];
    guest.latestUpdate = new Date().toLocaleString();
    guest.isComing = !guest.isComing;
    setFamily({...family});
  }

  function handleAddDiet(index: number) {
    const guest = family.members[index];
    guest.hasDiet = !guest.hasDiet;
    setFamily({...family});
  }

  function handleDietChange(index: number, diet: string) {
    const guest = family.members[index];
    guest.latestUpdate = new Date().toLocaleString();
    guest.dietaryPreference = diet;
    setFamily({...family});
  }

  return (
    <Container fluid id="invite">
      <Row id="gold-text">
        <Col>
          <h1>Dear {family.welcomeMsg}</h1>
          <p>
          Love is in the air, and we are thrilled to share our joy with you!
          </p>
          <p>
          Your presence will make our day even more memorable
          </p>
        </Col>
      </Row>
      <Row >
          <Form id="guests" onSubmit={e => onSubmitAttendance(e)}>
            <Row>
              <Col>
                Guest
              </Col>
              <Col>
              Is coming
              </Col>
              <Col>
              Allergies/diet
              </Col>
            </Row>
              {family.members.map((g: Guest, i: number) => {
                  return(
                    <Row key={"guest " +i}>
                      <Col>
                        <Form.Text>
                        {g.name}
                        </Form.Text>
                      </Col>
                      <Col> 
                        <Form.Check 
                          type="checkbox"
                          checked={g.isComing}
                          onChange={() => handleAttendanceChange(i)}
                          />
                      </Col>
                      <Col>
                      <Form.Group>
                        <Button hidden={g.hasDiet} onClick={() => handleAddDiet(i)}>
                        +
                        </Button>
                          <Form.Control hidden={!g.hasDiet} placeholder="Vegetarian" maxLength={30} value={g.dietaryPreference} onChange={e => {
                            handleDietChange(i, e.currentTarget.value)
                          }} />
                      </Form.Group>
                      </Col>
                    </Row>
                    
                  )
              })}
           <Row>
            <Form.Text>
            {family.lastResponseDate.trim() === "" ? "Response has not been received yet" : "Last response received on " + family.lastResponseDate }
            </Form.Text>
          </Row>
          <Button type="submit">
              <Spinner 
              hidden={!loading}
              as="span"
              animation="border"
              size="sm"
              role="status"
              aria-hidden="true"
            />
            <div hidden={loading}><img src={envelope}/></div>
          </Button>
        </Form>
        </Row>
  </Container>
  )
}

export default App
