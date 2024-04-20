import { useEffect, useState } from "react"
import { Table } from "react-bootstrap";



export interface Guest {
    name: string;
    familyName: string;
    dietaryPreference: string;
    isComing: boolean;
    latestUpdate: string;
    hasDiet: boolean;
  }

const Guests = () => {
    const [guests, setGuests] = useState([])
    const getGuests = async () => {
      fetch("http://localhost:8080/guests")
      .then(res => res.json())
      .then(
          (result) => {                 
            setGuests(result)
          },
          (error) => {
            console.log(error)
            setGuests([])
          }
      )
    }
    useEffect(() => {
        getGuests();
    }, [])

    return(
      <Table bordered>
      <thead>
        <tr>
          <th>Name</th>
          <th>Family</th>
          <th>Is coming</th>
          <th>Diet</th>
          <th>Last updated</th>
        </tr>
      </thead>
      <tbody>
        {guests.map((g: Guest, i: number) => {return(
           <tr>
           <td>{g.name}</td>
           <td>{g.familyName}</td>
           <td>{g.isComing ? "+" : "-"}</td>
           <td>{g.dietaryPreference}</td>
           <td>{g.latestUpdate}</td>
         </tr>
        )})}
      </tbody>
    </Table>
  )
}

export default Guests